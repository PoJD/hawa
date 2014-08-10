package cz.pojd.homeautomation.hawa.outdoor;

import javax.annotation.Resource;
import javax.inject.Inject;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;

public class OutdoorDAOImpl implements OutdoorDAO {

    @Inject
    private Sensors outdoorReadSensors;
    @Resource(name = "outdoorLightControl")
    private Control outdoorLightControl;

    private Outdoor state = new Outdoor();

    @Override
    public synchronized Outdoor get() {
	state.reset();
	for (Sensor sensor : outdoorReadSensors.getWeatherSensors()) {
	    state.addAllReadings(sensor.readAll());
	}

	return state;
    }

    @Override
    public synchronized void save(Outdoor outdoor) {
	// not saving readings, only the "autolights" flag
	state.setAutoLights(outdoor.isAutoLights());

	// and change the related control now
	if (outdoor.isAutoLights()) {
	    outdoorLightControl.enable();
	} else {
	    outdoorLightControl.disable();
	}
    }
}

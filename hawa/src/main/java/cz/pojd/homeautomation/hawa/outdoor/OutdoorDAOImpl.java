package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;

public class OutdoorDAOImpl implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);

    @Inject
    private Sensors outdoorReadSensors;
    @Resource(name = "outdoorLightControl")
    private Control outdoorLightControl;

    private Outdoor outdoor = new Outdoor();

    /*
     * DAO API
     * 
     * @see cz.pojd.homeautomation.hawa.outdoor.OutdoorDAO
     */

    @Override
    public Outdoor get() {
	return outdoor;
    }

    @Override
    public void save(Outdoor outdoor) {
	// not saving readings, only the "autolights" flag
	this.outdoor.setAutoLights(outdoor.isAutoLights());

	// and change the related control now
	if (this.outdoor.isAutoLights()) {
	    outdoorLightControl.enable();
	} else {
	    outdoorLightControl.disable();
	}
    }

    /*
     * Scheduler logic
     */

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void update() {
	LOG.info("Update triggered in OutdoorDAOImpl - detecting all outdoor sensors and saving them...");
	detectOutdoor();
	saveLastOutdoor();
	LOG.info("Update in OutdoorDAOImpl finished.");
    }

    private void detectOutdoor() {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Detecting outdoor state now...");
	}

	List<Reading> readings = new ArrayList<>();
	for (Sensor sensor : outdoorReadSensors.getWeatherSensors()) {
	    readings.addAll(sensor.readAll());
	}
	resetState(readings);

	if (LOG.isDebugEnabled()) {
	    LOG.debug("Outdoor state detected: " + readings);
	}
    }

    private void saveLastOutdoor() {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Saving last outdoor now...");
	}
	// TODO save to DB here
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Last outdoor saved.");
	}
    }

    private synchronized void resetState(Collection<Reading> readings) {
	outdoor.reset();
	outdoor.addAllReadings(readings);
    }
}

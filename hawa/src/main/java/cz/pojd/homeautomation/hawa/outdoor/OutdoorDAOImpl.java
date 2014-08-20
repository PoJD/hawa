package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;

import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;

public class OutdoorDAOImpl extends RefreshableDAO implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);

    @Inject
    private Sensors outdoorReadSensors;
    @Resource(name = "outdoorLightControl")
    private Control outdoorLightControl;

    private Outdoor outdoor = new Outdoor();

    @Inject
    public OutdoorDAOImpl(Refresher refresher) {
	super(refresher);
    }

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

    @Override
    protected void detectState() {
	List<Reading> readings = new ArrayList<>();
	for (Sensor sensor : outdoorReadSensors.getWeatherSensors()) {
	    readings.addAll(sensor.readAll());
	}
	resetState(readings);

	if (LOG.isDebugEnabled()) {
	    LOG.debug("Outdoor state detected: " + readings);
	}
    }

    @Override
    protected void saveState(LocalDateTime dateTime) {
	// TODO save to DB here
    }

    private synchronized void resetState(Collection<Reading> readings) {
	outdoor.reset();
	outdoor.addAllReadings(readings);
    }
}

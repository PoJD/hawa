package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;

@Repository
public class OutdoorDAOImpl extends RefreshableDAO implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);
    private static final String SQL = "insert into outdoor(name, at, reading) values (?, ?, ?)";

    @Inject
    private Sensors outdoorReadSensors;
    @Resource(name = "outdoorLightControl")
    private Control outdoorLightControl;

    private Outdoor outdoor = new Outdoor();

    public Sensors getOutdoorReadSensors() {
	return outdoorReadSensors;
    }

    public void setOutdoorReadSensors(Sensors outdoorReadSensors) {
	this.outdoorReadSensors = outdoorReadSensors;
    }

    public Control getOutdoorLightControl() {
	return outdoorLightControl;
    }

    public void setOutdoorLightControl(Control outdoorLightControl) {
	this.outdoorLightControl = outdoorLightControl;
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
	get().setAutoLights(outdoor.isAutoLights());

	// and change the related control now
	if (get().isAutoLights()) {
	    getOutdoorLightControl().enable();
	} else {
	    getOutdoorLightControl().disable();
	}
    }

    /*
     * Scheduler logic
     */

    @Override
    protected void detectState() {
	List<Reading> readings = new ArrayList<>();
	for (Sensor sensor : getOutdoorReadSensors().getWeatherSensors()) {
	    readings.addAll(sensor.readAll());
	}
	resetState(readings);

	if (LOG.isDebugEnabled()) {
	    LOG.debug("Outdoor state detected: " + readings);
	}
    }

    @Override
    protected void saveState(Date date) {
	if (get().getSensorReadings().isEmpty()) {
	    LOG.warn("Nothing to save, outdoor readings is an empty list.");
	    return;
	}

	// first create all data we want to insert
	List<Object[]> arguments = new ArrayList<>();
	for (Reading reading : get().getSensorReadings()) {
	    arguments.add(new Object[] { reading.getType().toString(), date, reading.getDoubleValue() });
	}

	getJdbcTemplate().batchUpdate(SQL, arguments);
    }

    private synchronized void resetState(Collection<Reading> readings) {
	get().reset();
	get().addAllReadings(readings);
    }
}

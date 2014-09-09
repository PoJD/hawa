package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;

@Repository
public class OutdoorDAOImpl extends RefreshableDAO implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);
    private static final String SQL = "insert into outdoor(name, at, reading) values (?, ?, ?)";

    @Inject
    private Outdoor outdoor;
    private OutdoorDetail outdoorDetail = new OutdoorDetail();

    /*
     * DAO API
     * 
     * @see cz.pojd.homeautomation.hawa.outdoor.OutdoorDAO
     */

    public Outdoor getOutdoor() {
	return outdoor;
    }

    public void setOutdoor(Outdoor outdoor) {
	this.outdoor = outdoor;
    }

    @Override
    public OutdoorDetail get() {
	return outdoorDetail;
    }

    @Override
    public void save(OutdoorDetail outdoorDetail) {
	// not saving readings, only the "autolights" flag
	this.outdoor.setAutolightsEnabled(outdoorDetail.isAutoLights());
	this.outdoorDetail.setAutoLights(outdoorDetail.isAutoLights());
    }

    /*
     * Scheduler logic
     */

    @Override
    protected void detectState() {
	List<Reading> readings = new ArrayList<>();
	for (Sensor sensor : outdoor.getOutdoorSensors()) {
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

    private synchronized void resetState(List<Reading> sensorReadings) {
	get().setSensorReadings(sensorReadings);
    }
}

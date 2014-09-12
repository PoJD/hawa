package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import cz.pojd.homeautomation.hawa.outdoor.factory.OutdoorFactory;
import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.homeautomation.hawa.spring.OutdoorSpecification;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;

@Repository
public class OutdoorDAOImpl extends RefreshableDAO implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);
    private static final String SQL = "insert into outdoor(name, at, reading) values (?, ?, ?)";

    private Outdoor outdoor;
    private OutdoorDetail outdoorDetail = new OutdoorDetail();

    /*
     * DAO API
     * 
     * @see cz.pojd.homeautomation.hawa.outdoor.OutdoorDAO
     */

    public OutdoorDAOImpl(OutdoorFactory outdoorFactory, OutdoorSpecification outdoorSpecification) {
	this.outdoor = outdoorFactory.create(outdoorSpecification);
    }

    @Override
    public OutdoorDetail get() {
	return outdoorDetail;
    }

    @Override
    public void save(OutdoorDetail outdoorDetail) {
	this.outdoor.resetFrom(outdoorDetail);
	this.outdoorDetail.resetFrom(outdoorDetail);
    }

    /*
     * Scheduler logic
     */

    @Override
    protected void detectState() {
	List<Reading> readings = new ArrayList<>();
	for (Sensor sensor : outdoor.getSensors()) {
	    readings.addAll(sensor.readAll());
	}
	resetState(new OutdoorDetail(outdoor, readings));

	if (LOG.isDebugEnabled()) {
	    LOG.debug("Outdoor state detected: " + get());
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

    private void resetState(OutdoorDetail outdoorDetail) {
	this.outdoorDetail = outdoorDetail;
    }
}

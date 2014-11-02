package cz.pojd.homeautomation.model.outdoor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cz.pojd.homeautomation.model.graphs.GraphData;
import cz.pojd.homeautomation.model.outdoor.factory.OutdoorFactory;
import cz.pojd.homeautomation.model.refresh.RefreshableDAO;
import cz.pojd.homeautomation.model.spring.OutdoorSpecification;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;

@Repository
public class OutdoorDAOImpl extends RefreshableDAO implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);
    private static final int DAYS_BACK_HISTORY = 7;

    private Outdoor outdoor;

    @Value("${sql.outdoorDAO.insert}")
    private String insertSql;
    @Value("${sql.outdoorDAO.outdoorHistory}")
    private String outdoorHistorySql;

    public String getInsertSql() {
	return insertSql;
    }

    public void setInsertSql(String insertSql) {
	this.insertSql = insertSql;
    }

    public String getOutdoorHistorySql() {
	return outdoorHistorySql;
    }

    public void setOutdoorHistorySql(String outdoorHistorySql) {
	this.outdoorHistorySql = outdoorHistorySql;
    }

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
	return outdoor.getLastDetail();
    }

    @Override
    public OutdoorDetail getWithHistory() {
	// create a new copy here - we do not want to store all the below data to memory now...
	OutdoorDetail detail = new OutdoorDetail(get());
	detail.setOutdoorHistory(new GraphData[] { getGraphData("Temperature °C", Type.temperature), getGraphData("Humidity %", Type.humidity),
		getGraphData("Pressure hPa", Type.pressure) });
	return detail;
    }

    @Override
    public void save(OutdoorDetail outdoorDetail) {
	this.outdoor.resetFrom(outdoorDetail);
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

	OutdoorDetail detail = new OutdoorDetail(outdoor);
	detail.setSensorReadings(readings);
	detail.setLastUpdate(getRefresher().getLastUpdate());

	resetState(detail);

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

	getJdbcTemplate().batchUpdate(getInsertSql(), arguments);
    }

    private void resetState(OutdoorDetail outdoorDetail) {
	this.outdoor.setLastDetail(outdoorDetail);
    }

    private GraphData getGraphData(String key, Type type) {
	List<Object[]> list = new ArrayList<>();
	try {
	    for (Map<String, Object> row : getJdbcTemplate().queryForList(outdoorHistorySql, new Object[] { type.toString(), DAYS_BACK_HISTORY })) {
		list.add(new Object[] { row.get("at"), row.get("reading") });
	    }
	} catch (Exception e) {
	    throw new OutdoorDAOException("Unable to detect history for sensor '" + type + "' for past " + DAYS_BACK_HISTORY + " days.", e);
	}

	GraphData result = new GraphData();
	result.setKey(key);
	result.setValues(list.toArray(new Object[][] {}));

	return result;
    }
}

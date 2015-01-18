package cz.pojd.homeautomation.model.outdoor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import cz.pojd.homeautomation.model.dao.StorageCleanup;
import cz.pojd.homeautomation.model.outdoor.factory.OutdoorFactory;
import cz.pojd.homeautomation.model.refresh.RefreshableDAO;
import cz.pojd.homeautomation.model.spring.OutdoorSpecification;
import cz.pojd.homeautomation.model.web.GraphData;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;

public class OutdoorDAOImpl extends RefreshableDAO implements OutdoorDAO {

    private static final Log LOG = LogFactory.getLog(OutdoorDAOImpl.class);

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

    public OutdoorDAOImpl(OutdoorFactory outdoorFactory, OutdoorSpecification outdoorSpecification, StorageCleanup storageCleanup) {
	this.outdoor = outdoorFactory.create(outdoorSpecification);
	storageCleanup.registerTable("outdoor");
    }

    @Override
    public OutdoorDetail get() {
	return getOutdoor().getLastDetail();
    }

    @Override
    public Outdoor getOutdoor() {
	return outdoor;
    }

    @Override
    public OutdoorDetail getWithHistory() {
	// create a new copy here - we do not want to store all the below data to memory now...
	OutdoorDetail detail = new OutdoorDetail(get());
	detail.setOutdoorHistory(getGraphData());
	return detail;
    }

    @Override
    public void save(OutdoorDetail outdoorDetail) {
	getOutdoor().resetFrom(outdoorDetail);
    }

    /*
     * Scheduler logic
     */

    @Override
    protected void detectState() {
	OutdoorDetail detail = new OutdoorDetail(getOutdoor());
	for (Sensor sensor : getOutdoor().getSensors()) {
	    detail.setSensorReadings(sensor.readAll().toArray(new Reading[] {}));
	}
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
	    if (reading.isValid()) {
		arguments.add(new Object[] { reading.getType().toString(), date, reading.getDoubleValue() });
	    }
	}

	if (!arguments.isEmpty()) {
	    getJdbcTemplate().batchUpdate(getInsertSql(), arguments);
	} else {
	    LOG.warn("Nothing to save, no valid readings found.");
	}
    }

    private void resetState(OutdoorDetail outdoorDetail) {
	getOutdoor().setLastDetail(outdoorDetail);
    }

    private GraphData[] getGraphData() {
	GraphData[] result = new GraphData[] { new GraphData(TEMPERATURE_DESC), new GraphData(HUMIDITY_DESC), new GraphData(PRESSURE_DESC) };

	Map<String, List<Object[]>> lists = new HashMap<>();
	lists.put(Type.temperature.toString(), new ArrayList<Object[]>());
	lists.put(Type.humidity.toString(), new ArrayList<Object[]>());
	lists.put(Type.pressure.toString(), new ArrayList<Object[]>());

	try {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("About to run SQL '" + outdoorHistorySql + "' for outdoor.");
	    }
	    for (Map<String, Object> row : getJdbcTemplate().queryForList(outdoorHistorySql, new Object[] { DAYS_BACK_HISTORY })) {
		Object name = row.get("name");
		List<Object[]> list = lists.get(name);
		if (list != null) {
		    list.add(new Object[] { row.get("at"), row.get("reading") });
		} else {
		    LOG.warn("Unknown type/name of reading found in SQL: " + name + ". Ignoring this row...");
		}
	    }
	    if (LOG.isDebugEnabled()) {
		LOG.debug("SQL ran OK");
	    }
	} catch (Exception e) {
	    throw new OutdoorDAOException("Unable to detect history for outdoor for past " + DAYS_BACK_HISTORY + " days.", e);
	}

	result[0].setValues(lists.get(Type.temperature.toString()).toArray(new Object[][] {}));
	result[1].setValues(lists.get(Type.humidity.toString()).toArray(new Object[][] {}));
	result[2].setValues(lists.get(Type.pressure.toString()).toArray(new Object[][] {}));

	return result;
    }
}

package cz.pojd.homeautomation.hawa.rooms;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cz.pojd.homeautomation.hawa.graphs.GraphData;
import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.homeautomation.hawa.rooms.factory.RoomFactory;
import cz.pojd.homeautomation.hawa.spring.RoomSpecification;
import cz.pojd.rpi.system.RuntimeExecutor;

/**
 * Rooms DAO implementation.
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 10:58:59 PM
 */
@Repository
public class RoomsDAOImpl extends RefreshableDAO implements RoomsDAO {

    private static final Log LOG = LogFactory.getLog(RoomsDAOImpl.class);

    private final Map<String, Room> rooms;
    private final Map<String, RoomDetail> roomDetails;

    @Value("${sql.roomsDAO.insert}")
    private String insertSql;
    @Value("${sql.roomsDAO.temperatureHistory}")
    private String temperatureHistorySql;

    public String getInsertSql() {
	return insertSql;
    }

    public void setInsertSql(String insertSql) {
	this.insertSql = insertSql;
    }

    public String getTemperatureHistorySql() {
	return temperatureHistorySql;
    }

    public void setTemperatureHistorySql(String temperatureHistorySql) {
	this.temperatureHistorySql = temperatureHistorySql;
    }

    /*
     * API Implementation
     * 
     * @see cz.pojd.homeautomation.hawa.rooms.RoomsDAO
     */

    @Inject
    public RoomsDAOImpl(RoomFactory factory, RoomSpecification[] roomSpecifications, RuntimeExecutor runtimeExecutor) {
	rooms = new LinkedHashMap<>();
	roomDetails = new LinkedHashMap<>();
	for (RoomSpecification roomSpecification : roomSpecifications) {
	    rooms.put(roomSpecification.getName(), factory.create(roomSpecification));
	}
    }

    /**
     * Synchronized against the update's method part updating the list...
     */
    @Override
    public synchronized List<RoomDetail> query() {
	return new ArrayList<>(roomDetails.values());
    }

    @Override
    public void save(RoomDetail roomDetail) {
	Room room = rooms.get(roomDetail.getName());
	if (room != null) {
	    room.resetFrom(roomDetail);
	} else {
	    LOG.warn("Unable to find room by name " + roomDetail.getName() + ". Not updating any room in save().");
	}

	RoomDetail detail = roomDetails.get(roomDetail.getName());
	if (detail != null) {
	    detail.resetFrom(roomDetail);
	} else {
	    LOG.warn("Unable to find room detail by name " + roomDetail.getName() + ". Not updating any room detail in save().");
	}
    }

    @Override
    public RoomDetail get(String roomName) {
	RoomDetail roomDetail = roomDetails.get(roomName);
	if (roomDetail != null) {
	    // create a new copy here - we do not want to store all the below data to memory now...
	    RoomDetail detail = new RoomDetail(roomDetail);
	    // TODO does the color really belong here?
	    detail.setTemperatureHistory(new GraphData[] { getGraphData("Last 24 hours", "#0f0", 1, roomName),
		    getGraphData("Last week", "#f00", 7, roomName) });
	    return detail;
	} else {
	    LOG.warn("Unable to find roomDetail by name " + roomName + ". Returning null in method get().");
	}
	return null;
    }

    /*
     * Scheduler logic
     */

    @Override
    protected void saveState(Date date) {
	// first create all data we want to insert
	List<Object[]> arguments = new ArrayList<>();
	for (RoomDetail roomDetail : roomDetails.values()) {
	    if (roomDetail.isValidTemperature()) {
		arguments.add(new Object[] { roomDetail.getName(), date, roomDetail.getTemperature().getDoubleValue() });
	    }
	}

	if (!arguments.isEmpty()) {
	    getJdbcTemplate().batchUpdate(insertSql, arguments);
	} else {
	    LOG.warn("Nothing to save, no valid temperature readings found.");
	}
    }

    @Override
    protected void detectState() {
	Map<String, RoomDetail> roomDetails = new LinkedHashMap<>();
	for (Entry<String, Room> roomEntry : rooms.entrySet()) {
	    String key = roomEntry.getKey();
	    Room room = roomEntry.getValue();
	    RoomDetail detail = new RoomDetail(room);
	    detail.setLastUpdate(getRefresher().getLastUpdate());
	    roomDetails.put(key, detail);
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Room state detected: " + detail);
	    }
	}
	resetState(roomDetails);
    }

    /*
     * Other stuff
     */

    private synchronized void resetState(Map<String, RoomDetail> roomDetails) {
	this.roomDetails.clear();
	this.roomDetails.putAll(roomDetails);
    }

    private GraphData getGraphData(String key, String color, int daysBack, String roomName) {
	List<Object[]> list = new ArrayList<>();
	try {
	    for (Map<String, Object> row : getJdbcTemplate().queryForList(temperatureHistorySql, new Object[] { roomName, daysBack })) {
		list.add(new Object[] { row.get("at"), row.get("temperature") });
	    }
	} catch (Exception e) {
	    throw new RoomsDAOException("Unable to detect history for room '" + roomName + "' for past " + daysBack + " days.", e);
	}

	GraphData result = new GraphData();
	result.setKey(key);
	result.setColor(color);
	result.setValues(list.toArray(new Object[][] {}));
	return result;
    }
}

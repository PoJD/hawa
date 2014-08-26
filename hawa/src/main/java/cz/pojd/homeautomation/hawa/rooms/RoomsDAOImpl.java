package cz.pojd.homeautomation.hawa.rooms;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cz.pojd.homeautomation.hawa.graphs.GraphData;
import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.w1.Ds18B20TemperatureSensor;
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
    public RoomsDAOImpl(List<RoomSpecification> roomSpecifications, RuntimeExecutor runtimeExecutor) {
	rooms = new LinkedHashMap<>();
	for (RoomSpecification roomSpecification : roomSpecifications) {
	    Room room = new Room();
	    room.setName(roomSpecification.getName());
	    room.setTemperatureSensor(new Ds18B20TemperatureSensor(runtimeExecutor, roomSpecification.getTemperatureID()));
	    room.setAutoLights(roomSpecification.getAutolights());
	    room.setFloor(roomSpecification.getFloor());
	    rooms.put(room.getName(), room);
	}
    }

    /**
     * Synchronized against the update's method part updating the list...
     */
    @Override
    public List<RoomDetail> query() {
	List<RoomDetail> result = new ArrayList<>();
	for (Room room : rooms.values()) {
	    if (room.getRoomDetail() != null) {
		result.add(room.getRoomDetail());
	    }
	}
	return result;
    }

    @Override
    public void save(RoomDetail roomState) {
	Room room = rooms.get(roomState.getName());
	if (room != null) {
	    // the only item that can change is the autolights....
	    room.setAutoLights(roomState.getAutoLights());
	} else {
	    LOG.warn("Unable to find room by name " + roomState.getName() + ". Ignoring the call to save.");
	}
    }

    @Override
    public RoomDetail get(String roomName) {
	Room room = rooms.get(roomName);
	if (room != null) {
	    // create a new copy here - we do not want to store all the below data to memory now...
	    RoomDetail detail = new RoomDetail(room.getRoomDetail());
	    // TODO does the color really belong here?
	    detail.setTemperatureHistory(new GraphData[] { getGraphData("Last 24 hours", "#0f0", 1, roomName),
		    getGraphData("Last week", "#f00", 7, roomName) });
	    return detail;
	} else {
	    LOG.warn("Unable to find room by name " + roomName + ". Returning null in method get().");
	    return null;
	}
    }

    /*
     * Scheduler logic
     */

    @Override
    protected void saveState(Date date) {
	if (rooms.isEmpty()) {
	    LOG.warn("Nothing to save, no rooms found.");
	    return;
	}
	// first create all data we want to insert
	List<Object[]> arguments = new ArrayList<>();
	for (Room room : rooms.values()) {
	    arguments.add(new Object[] { room.getName(), date, room.getRoomDetail() != null ? room.getRoomDetail().getRawTemperature() : "0" });
	}

	getJdbcTemplate().batchUpdate(insertSql, arguments);
    }

    @Override
    protected synchronized void detectState() {
	for (Room room : rooms.values()) {
	    RoomDetail state = new RoomDetail();
	    state.setName(room.getName());
	    state.setAutoLights(room.getAutoLights());
	    Reading reading = room.getTemperatureSensor().read();
	    state.setRawTemperature(reading.getDoubleValue());
	    state.setTemperature(reading.getStringValue());
	    state.setFloor(room.getFloor());
	    state.setLastUpdate(getRefresher().getLastUpdate());
	    room.setRoomDetail(state);
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Rooms state detected: " + state);
	    }
	}
    }

    /*
     * Other stuff
     */
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

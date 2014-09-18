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
    private static final int DAYS_BACK_HISTORY = 7;

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
    public RoomsDAOImpl(RoomFactory factory, RoomSpecification[] roomSpecifications, RuntimeExecutor runtimeExecutor) {
	rooms = new LinkedHashMap<>();
	for (RoomSpecification roomSpecification : roomSpecifications) {
	    rooms.put(roomSpecification.getName(), factory.create(roomSpecification));
	}
    }

    @Override
    public List<RoomDetail> query() {
	// no synchronization needed, since we just built a list populated from the rooms, in worst case (if just in the middle of refresh, we might
	// be getting older details here, but these should be updated anyway, so no net effect really)
	List<RoomDetail> result = new ArrayList<>();
	for (Room room : rooms.values()) {
	    result.add(room.getLastDetail());
	}

	return result;
    }

    @Override
    public void save(RoomDetail roomDetail) {
	Room room = rooms.get(roomDetail.getName());
	if (room != null) {
	    room.resetFrom(roomDetail);
	} else {
	    LOG.warn("Unable to find room by name " + roomDetail.getName() + ". Not updating any room in save().");
	}
    }

    @Override
    public RoomDetail get(String roomName) {
	Room room = rooms.get(roomName);
	if (room != null) {
	    // create a new copy here - we do not want to store all the below data to memory now...
	    RoomDetail detail = new RoomDetail(room.getLastDetail());
	    // TODO does the color really belong here?
	    detail.setTemperatureHistory(new GraphData[] { getGraphData("Temperature", "#700", roomName) });
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
	for (Room room : rooms.values()) {
	    RoomDetail roomDetail = room.getLastDetail();
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
	for (Room room : rooms.values()) {
	    RoomDetail detail = new RoomDetail(room);
	    detail.setLastUpdate(getRefresher().getLastUpdate());
	    room.setLastDetail(detail);
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Room state detected: " + detail);
	    }
	}
    }

    /*
     * Other stuff
     */

    private GraphData getGraphData(String key, String color, String roomName) {
	List<Object[]> list = new ArrayList<>();
	try {
	    for (Map<String, Object> row : getJdbcTemplate().queryForList(temperatureHistorySql, new Object[] { roomName, DAYS_BACK_HISTORY })) {
		list.add(new Object[] { row.get("at"), row.get("temperature") });
	    }
	} catch (Exception e) {
	    throw new RoomsDAOException("Unable to detect history for room '" + roomName + "' for past " + DAYS_BACK_HISTORY + " days.", e);
	}

	GraphData result = new GraphData();
	result.setKey(key);
	result.setColor(color);
	result.setValues(list.toArray(new Object[][] {}));
	return result;
    }
}

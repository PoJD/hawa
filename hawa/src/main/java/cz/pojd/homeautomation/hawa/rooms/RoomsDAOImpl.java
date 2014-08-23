package cz.pojd.homeautomation.hawa.rooms;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

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
    private static final String SQL = "insert into roomstate(name, when, temperature) values (?, ?, ?)";

    private final Map<String, Room> rooms;

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
    public List<RoomState> getAll() {
	List<RoomState> result = new ArrayList<>();
	for (Room room : rooms.values()) {
	    if (room.getRoomState() != null) {
		result.add(room.getRoomState());
	    }
	}
	return result;
    }

    @Override
    public void save(RoomState roomState) {
	Room room = rooms.get(roomState.getName());
	if (room != null) {
	    // the only item that can change is the autolights....
	    room.setAutoLights(roomState.getAutoLights());
	} else {
	    LOG.warn("Unable to find room by name " + roomState.getName() + ". Ignoring the call to save.");
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
	    arguments.add(new Object[] { room.getName(), date, room.getRoomState() != null ? room.getRoomState().getRawTemperature() : "0" });
	}

	jdbcTemplate.batchUpdate(SQL, arguments);
    }

    @Override
    protected synchronized void detectState() {
	for (Room room : rooms.values()) {
	    RoomState state = new RoomState();
	    state.setName(room.getName());
	    state.setAutoLights(room.getAutoLights());
	    Reading reading = room.getTemperatureSensor().read();
	    state.setRawTemperature(reading.getDoubleValue());
	    state.setTemperature(reading.getStringValue());
	    state.setFloor(room.getFloor());
	    room.setRoomState(state);
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Rooms state detected: " + state);
	    }
	}
    }
}

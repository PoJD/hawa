package cz.pojd.homeautomation.hawa.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.w1.Ds18B20TemperatureSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

/**
 * Rooms DAO implementation.
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 10:58:59 PM
 */
public class RoomsDAOImpl implements RoomsDAO {

    private static final Log LOG = LogFactory.getLog(RoomsDAOImpl.class);
    private final Map<String, Room> rooms;

    @Inject
    public RoomsDAOImpl(List<RoomSpecification> roomSpecifications, RuntimeExecutor runtimeExecutor) {
	rooms = new HashMap<>();
	for (RoomSpecification roomSpecification : roomSpecifications) {
	    Room room = new Room();
	    room.setName(roomSpecification.getName());
	    room.setTemperatureSensor(new Ds18B20TemperatureSensor(runtimeExecutor, roomSpecification.getTemperatureID()));
	    rooms.put(room.getName(), room);
	}
    }

    @Override
    public List<RoomState> getAll() {
	List<RoomState> result = new ArrayList<>();
	for (Room room : rooms.values()) {
	    RoomState state = new RoomState();
	    state.setName(room.getName());
	    state.setAutoLights(room.isAutoLights());
	    state.setTemperature(room.getTemperatureSensor().read().getValue());
	    result.add(state);
	}
	return result;
    }

    @Override
    public synchronized void save(RoomState roomState) {
	Room room = rooms.get(roomState.getName());
	if (room != null) {
	    room.setAutoLights(roomState.isAutoLights());
	} else {
	    LOG.warn("Unable to find room by name " + roomState.getName() + ". Ignoring the call to save.");
	}
    }
}

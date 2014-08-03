package cz.pojd.homeautomation.hawa.rooms;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cz.pojd.rpi.sensors.Ds18B20TemperatureSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

/**
 * Rooms DAO implementation.
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 10:58:59 PM
 */
public class RoomsDAOImpl implements RoomsDAO {

    private final List<Room> rooms;

    @Inject
    public RoomsDAOImpl(List<RoomSpecification> roomSpecifications, RuntimeExecutor runtimeExecutor) {
	rooms = new ArrayList<>();
	for (RoomSpecification roomSpecification : roomSpecifications) {
	    Room room = new Room();
	    room.setName(roomSpecification.getName());
	    room.setTemperatureSensor(new Ds18B20TemperatureSensor(runtimeExecutor, roomSpecification.getTemperatureID()));
	    rooms.add(room);
	}
    }

    @Override
    public List<RoomState> getAll() {
	List<RoomState> result = new ArrayList<>();
	for (Room room : rooms) {
	    RoomState state = new RoomState();
	    state.setName(room.getName());
	    state.setAutoLights(room.isAutoLights());
	    state.setTemperature(room.getTemperatureSensor().read().getValue());
	    result.add(state);
	}
	return result;
    }

    @Override
    public void save(RoomState room) {
    }
}

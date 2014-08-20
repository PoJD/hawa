package cz.pojd.homeautomation.hawa.rooms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;

import cz.pojd.homeautomation.hawa.refresh.RefreshableDAO;
import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.rpi.sensors.w1.Ds18B20TemperatureSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

/**
 * Rooms DAO implementation.
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 10:58:59 PM
 */
public class RoomsDAOImpl extends RefreshableDAO implements RoomsDAO {

    private static final Log LOG = LogFactory.getLog(RoomsDAOImpl.class);

    private final Map<String, Room> rooms;
    private final List<RoomState> roomStates;

    /*
     * API Implementation
     * 
     * @see cz.pojd.homeautomation.hawa.rooms.RoomsDAO
     */

    @Inject
    public RoomsDAOImpl(List<RoomSpecification> roomSpecifications, RuntimeExecutor runtimeExecutor, Refresher refresher) {
	super(refresher);
	rooms = new LinkedHashMap<>();
	roomStates = new ArrayList<>();
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
    public synchronized List<RoomState> getAll() {
	// simply return new list each time based on the last cached list - could be empty in the very beginning...
	return Collections.unmodifiableList(new ArrayList<>(roomStates));
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
    protected void saveState(LocalDateTime dateTime) {
	// TODO save to DB here
    }

    @Override
    protected void detectState() {
	List<RoomState> newStates = new ArrayList<>();
	for (Room room : rooms.values()) {
	    RoomState state = new RoomState();
	    state.setName(room.getName());
	    state.setAutoLights(room.getAutoLights());
	    state.setTemperature(room.getTemperatureSensor().read().getValue());
	    state.setFloor(room.getFloor());
	    newStates.add(state);
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("All rooms states detected: " + newStates);
	}
	updateRoomStates(newStates);
    }

    private synchronized void updateRoomStates(List<RoomState> newStates) {
	roomStates.clear();
	roomStates.addAll(newStates);
    }
}

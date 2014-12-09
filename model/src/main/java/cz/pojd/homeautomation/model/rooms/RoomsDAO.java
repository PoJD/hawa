package cz.pojd.homeautomation.model.rooms;

import java.util.Collection;

import cz.pojd.homeautomation.model.dao.DAO;
import cz.pojd.homeautomation.model.refresh.Refreshable;
import cz.pojd.homeautomation.model.spring.RoomSpecification;
import cz.pojd.rpi.controllers.Observer;

/**
 * DAO for Rooms
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 6:21:24 PM
 */
public interface RoomsDAO extends Refreshable, DAO {

    /**
     * Get all room details
     * 
     * @return collection of all room details
     */
    Collection<RoomDetail> query();

    /**
     * Save the room
     * 
     * @param room
     *            room to save
     * @return true if the save was successful
     */
    void save(RoomDetail roomDetail);

    /**
     * Get the detail of the in passed room
     * 
     * @param roomName
     * @return
     */
    RoomDetail get(String roomName);

    /**
     * Get the room by its name
     * 
     * @param roomSpecification
     * @return room
     */
    Room getRoom(RoomSpecification roomSpecification);

    /**
     * Switches of all lights of all rooms
     */
    void switchOffAllLights();

    /**
     * Add observer for changes in rooms - new room details created to wrap information about the rooms
     * 
     * @param observer
     *            observer to be added and to be fired anytime a room detail is created for a room
     */
    void addObserver(Observer<RoomsDAO, RoomDetail> observer);
}

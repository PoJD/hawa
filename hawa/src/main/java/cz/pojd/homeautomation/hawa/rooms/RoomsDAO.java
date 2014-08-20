package cz.pojd.homeautomation.hawa.rooms;

import java.util.List;

import cz.pojd.homeautomation.hawa.refresh.Refreshable;

/**
 * DAO for Rooms
 *
 * @author Lubos Housa
 * @since Jul 27, 2014 6:21:24 PM
 */
public interface RoomsDAO extends Refreshable {

    /**
     * Get all rooms
     * @return list of all rooms
     */
    List<RoomState> getAll();
    
    /**
     * Save the room
     * @param room room to save
     * @return true if the save was successful
     */
    void save(RoomState room);
}

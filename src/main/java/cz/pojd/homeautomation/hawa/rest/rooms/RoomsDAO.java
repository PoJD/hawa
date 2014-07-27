package cz.pojd.homeautomation.hawa.rest.rooms;

import java.util.List;

/**
 * DAO for Rooms
 *
 * @author Lubos Housa
 * @since Jul 27, 2014 6:21:24 PM
 */
public interface RoomsDAO {

    /**
     * Get all rooms
     * @return list of all rooms
     */
    List<Room> getAll();
}

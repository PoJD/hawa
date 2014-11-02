package cz.pojd.homeautomation.model.rooms.factory;

import cz.pojd.homeautomation.model.rooms.Room;
import cz.pojd.homeautomation.model.spring.RoomSpecification;

/**
 * Factory for creating new rooms
 *
 * @author Lubos Housa
 * @since Sep 10, 2014 11:27:16 PM
 */
public interface RoomFactory {

    /**
     * Create new room given the specification
     * 
     * @param specification
     *            specification to base the new room on
     * @return new instance of room
     */
    Room create(RoomSpecification specification);
}

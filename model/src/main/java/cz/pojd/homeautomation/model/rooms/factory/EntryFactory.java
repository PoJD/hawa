package cz.pojd.homeautomation.model.rooms.factory;

import cz.pojd.homeautomation.model.rooms.Entry;
import cz.pojd.homeautomation.model.rooms.Room;
import cz.pojd.homeautomation.model.spring.EntrySpecification;

/**
 * Factory for creating new entries
 *
 * @author Lubos Housa
 * @since Dec 9, 2014 11:31:44 PM
 */
public interface EntryFactory {

    /**
     * Create new entry given the specification
     * 
     * @param specification
     *            specification to base the new entry on
     * @param room
     *            room to create the entry for
     * @return new instance of entry
     */
    Entry create(EntrySpecification specification, Room room);
}

package cz.pojd.security.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.rooms.Floor;
import cz.pojd.homeautomation.model.spring.RoomSpecification;

/**
 * Represents the source where a security event was raised
 *
 * @author Lubos Housa
 * @since Nov 8, 2014 11:31:17 PM
 */
public enum Source {
    KITCHEN_WEST_WINDOW(RoomSpecification.KITCHEN, "West kitchen window"),
    HALL_DOWN(RoomSpecification.HALL_DOWN),
    HALL_UP(RoomSpecification.HALL_UP),
    OUTDOOR("Outdoor"),
    CAMERA_MAINDOOR("Camera main door"),
    UNKNOWN("N/A");

    private static final Log LOG = LogFactory.getLog(Source.class);
    private final String description;
    private final Floor floor;

    private Source(String description) {
	this.description = description;
	this.floor = Floor.BASEMENT;
    }

    private Source(RoomSpecification room) {
	this(room, room.getName());
    }

    private Source(RoomSpecification room, String description) {
	this.description = description;
	this.floor = room.getFloor();
    }

    public String getDescription() {
	return description;
    }

    public Floor getFloor() {
	return floor;
    }

    public static Source parse(String source) {
	try {
	    return valueOf(source.toUpperCase());
	} catch (Exception e) {
	    LOG.error("Unable to parse value " + source + " into an existing source. Assuming unknown source...", e);
	    return UNKNOWN;
	}
    }
}

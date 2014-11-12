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
    CAMERA_HALL_DOWN("Camera hall down", false),
    HALL_DOWN(RoomSpecification.HALL_DOWN),
    HALL_UP(RoomSpecification.HALL_UP),
    OUTDOOR("Outdoor", true),
    CAMERA_MAINDOOR("Camera main door", true),
    UNKNOWN("N/A", false);

    private static final Log LOG = LogFactory.getLog(Source.class);
    private final String description;
    private final Floor floor;
    private final boolean outdoor;

    private Source(String description, boolean outdoor) {
	this(description, Floor.BASEMENT, outdoor);
    }

    private Source(RoomSpecification room) {
	this(room, room.getName());
    }

    private Source(RoomSpecification room, String description) {
	this(room.getName(), room.getFloor(), false);
    }

    private Source(String description, Floor floor, boolean outdoor) {
	this.description = description;
	this.floor = floor;
	this.outdoor = outdoor;
    }

    public String getDescription() {
	return description;
    }

    public Floor getFloor() {
	return floor;
    }

    public boolean isOutdoor() {
	return outdoor;
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

package cz.pojd.security.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.Source;
import cz.pojd.homeautomation.model.spring.RoomSpecification;

/**
 * Represents the source where a security event was raised - enumeration of all potential places except for rooms that have their own enumeration.
 *
 * @author Lubos Housa
 * @since Nov 8, 2014 11:31:17 PM
 */
public enum SecuritySource implements Source {
    KITCHEN_WEST_WINDOW(RoomSpecification.KITCHEN, "West kitchen window"),
    CAMERA_HALL_DOWN("Camera hall down", false),
    OUTDOOR("Outdoor", true),
    CAMERA_MAINDOOR("Camera main door", true),
    UNKNOWN("N/A", false);

    private static final Log LOG = LogFactory.getLog(SecuritySource.class);
    private final String description;
    private final Floor floor;
    private final boolean outdoor;

    private SecuritySource(String description, boolean outdoor) {
	this(description, Floor.BASEMENT, outdoor);
    }

    private SecuritySource(RoomSpecification room, String description) {
	this(description, room.getFloor(), false);
    }

    private SecuritySource(String description, Floor floor, boolean outdoor) {
	this.description = description;
	this.floor = floor;
	this.outdoor = outdoor;
    }

    @Override
    public String getName() {
	return name();
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public Floor getFloor() {
	return floor;
    }

    @Override
    public boolean isOutdoor() {
	return outdoor;
    }

    public static SecuritySource parse(String source) {
	try {
	    return valueOf(source.toUpperCase());
	} catch (Exception e) {
	    LOG.error("Unable to parse value " + source + " into an existing source. Assuming unknown source...", e);
	    return UNKNOWN;
	}
    }

    @Override
    public String toString() {
	return "SecuritySource [name=" + getName() + ", description=" + description + ", floor=" + floor + ", outdoor=" + outdoor + "]";
    }
}

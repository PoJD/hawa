package cz.pojd.security.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.spring.RoomSpecification;

/**
 * Represents the source where a security event was raised
 *
 * @author Lubos Housa
 * @since Nov 8, 2014 11:31:17 PM
 */
public enum Source {
    HALL_DOWN(RoomSpecification.HALL_DOWN.getName()), HALL_UP(RoomSpecification.HALL_UP.getName()), OUTDOOR("Outdoor"), CAMERA_MAINDOOR(
	    "Camera main door"), UNKNOWN("N/A");
    private static final Log LOG = LogFactory.getLog(Source.class);
    private final String description;

    private Source(String description) {
	this.description = description;
    }

    public String getDescription() {
	return description;
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

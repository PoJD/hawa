package cz.pojd.security.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.Source;

/**
 * Represents a camera source where a security event was raised - enumeration of all potential cameras in the application.
 *
 * @author Lubos Housa
 * @since Nov 8, 2014 11:31:17 PM
 */
public enum Camera implements Source {
    CAMERA_HALL_DOWN("Camera hall down", false),
    CAMERA_MAINDOOR("Camera main door", true),
    UNKNOWN("N/A", false);

    private static final Log LOG = LogFactory.getLog(Camera.class);
    private final String name;
    private final Floor floor;
    private final boolean outdoor;

    private Camera(String name, boolean outdoor) {
	this(name, Floor.Basement, outdoor);
    }

    private Camera(String name, Floor floor, boolean outdoor) {
	this.name = name;
	this.floor = floor;
	this.outdoor = outdoor;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getId() {
	return name();
    }

    @Override
    public Floor getFloor() {
	return floor;
    }

    @Override
    public boolean isOutdoor() {
	return outdoor;
    }

    public static Camera parse(String source) {
	try {
	    return valueOf(source.toUpperCase());
	} catch (Exception e) {
	    LOG.error("Unable to parse value " + source + " into an existing Camera source. Assuming unknown camera...", e);
	    return UNKNOWN;
	}
    }
}

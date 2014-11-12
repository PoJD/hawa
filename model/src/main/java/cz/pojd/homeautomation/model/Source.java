package cz.pojd.homeautomation.model;

/**
 * Generic source in the application. For example a room, outdoor, a specific camera, etc.
 *
 * @author Lubos Housa
 * @since Nov 12, 2014 6:38:36 PM
 */
public interface Source {

    /**
     * Gets the name of this source
     * 
     * @return string representation of this source
     */
    String getName();

    /**
     * Gets the Id of this source
     * 
     * @return string unique ID of this source
     */
    String getId();

    /**
     * Gets the floor of this source
     * 
     * @return floor of this source
     */
    Floor getFloor();

    /**
     * Detects whether this source represents an outdoor source or not
     * 
     * @return true if so, false otherwise
     */
    boolean isOutdoor();
}

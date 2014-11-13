package cz.pojd.rpi.system;

import org.joda.time.DateTime;

/**
 * Time service in the application
 *
 * @author Lubos Housa
 * @since Nov 13, 2014 10:45:15 PM
 */
public interface TimeService {

    /**
     * Returns current date time
     */
    DateTime now();
}

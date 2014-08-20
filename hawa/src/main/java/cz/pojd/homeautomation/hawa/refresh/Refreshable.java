package cz.pojd.homeautomation.hawa.refresh;

import org.joda.time.LocalDateTime;

/**
 * Generic interface denoting the implementations are refreshable by some means in the application
 * 
 * @author Lubos Housa
 * @since Aug 19, 2014 5:38:10 PM
 */
public interface Refreshable {

    /**
     * Refresh this object
     * 
     * @param dateTime
     *            current date time
     */
    public void refresh(LocalDateTime dateTime);
}

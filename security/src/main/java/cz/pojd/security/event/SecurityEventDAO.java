package cz.pojd.security.event;

import java.util.Collection;

import org.joda.time.DateTime;

import cz.pojd.homeautomation.model.DAO;

/**
 * DAO for retrieving/storing security events
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 1:56:29 PM
 */
public interface SecurityEventDAO extends DAO {

    /**
     * Get all security events in the specified date range
     * 
     * @param start
     *            start of the range
     * @param end
     *            end of the range
     * @return all security events for the specified time period
     */
    Collection<SecurityEvent> query(DateTime start, DateTime end);

    /**
     * Save the security event to the underlying storage
     * 
     * @param securityEvent
     *            security event to save
     */
    void save(SecurityEvent securityEvent);
}

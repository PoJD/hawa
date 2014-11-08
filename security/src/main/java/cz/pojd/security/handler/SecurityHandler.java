package cz.pojd.security.handler;

import cz.pojd.security.event.SecurityEvent;

/**
 * Security handler of an event - e.g. performs some action in the event of a security breach.
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 12:16:27 AM
 */
public interface SecurityHandler {

    /**
     * Handle the security event as a security breach
     * 
     * @param event
     *            event to handle
     */
    void handleSecurityBreach(SecurityEvent event);
}

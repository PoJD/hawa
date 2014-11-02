package cz.pojd.security.controller;

import cz.pojd.security.event.SecurityEvent;

/**
 * Core of the security module. Provides API for various sources to fire security events and manages processing of such security events.
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 9:19:03 PM
 */
public interface Controller {

    /**
     * Handle a new security event
     * 
     * @param securityEvent
     *            event to handle
     */
    void handle(SecurityEvent securityEvent);
}

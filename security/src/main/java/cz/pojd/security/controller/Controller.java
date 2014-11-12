package cz.pojd.security.controller;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.security.event.SecurityEvent;

/**
 * Core of the security module. Provides API for various sources to fire security events and manages processing of such security events.
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 9:19:03 PM
 */
public interface Controller {

    /**
     * Handle a new security event. Multiple events could be handled at the same time, thus this controller instance guarantees thread safety
     * 
     * @param securityEvent
     *            event to handle
     */
    void handle(SecurityEvent securityEvent);

    /**
     * Change the current security mode
     * 
     * @param newMode
     *            new mode to set the controller to
     */
    void switchMode(SecurityMode newMode);

    /**
     * Detects current security mode of thie controller
     * 
     * @return current security mode
     */
    SecurityMode getMode();

    /**
     * Add observer for changes in security mode
     * 
     * @param observer
     *            observer to be added and to be fired anytime the security mode is changed
     */
    void addObserver(Observer<Controller, SecurityMode> observer);
}

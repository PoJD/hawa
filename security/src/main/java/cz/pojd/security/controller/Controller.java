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
     * Returns the current security breach event. Anytime a security breach is detected, the associated security event would get processed and also be
     * active in the security controller for some predefined amount of time or until the user dismissed such event. This method therefore returns a
     * security breach if there is one now, otherwise returns null
     * 
     * @return current security breach or null if no security breach is currently being active
     */
    SecurityEvent getCurrentBreach();

    /**
     * Detects whether there is any security breach active at the moment. In other word checks whether the getCurrent() method returns non null
     * security event.
     * 
     * @return true if so, false otherwise
     * @see #getCurrentBreach()
     */
    boolean isBreach();

    /**
     * Dismiss the security breach if any active. This method has no effect if there is no security breach active at the moment. Otherwise it erases
     * the current security breach, i.e. the calls to getCurrent after this invocation (and before a new security breach is detected) should return
     * null.
     */
    void dismissBreach();

    /**
     * Add observer for changes in security mode
     * 
     * @param observer
     *            observer to be added and to be fired anytime the security mode is changed
     */
    void addObserver(Observer<Controller, SecurityMode> observer);
}

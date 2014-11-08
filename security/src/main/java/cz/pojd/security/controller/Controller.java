package cz.pojd.security.controller;

import java.util.Set;

import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.rules.Rule;

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

    /**
     * Change the current security mode
     * 
     * @param newMode
     *            new mode to set the controller to
     */
    void switchMode(SecurityMode newMode);

    /**
     * Register new security rule
     * 
     * @param rule
     *            new rule to register
     */
    void registerRule(Rule rule);

    /**
     * Get all registered rules
     * 
     * @return set of all registered rules
     */
    Set<Rule> getRegisteredRules();
}

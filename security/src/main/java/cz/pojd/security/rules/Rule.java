package cz.pojd.security.rules;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;

/**
 * A rule represents on of the rules in the security system. This typically means processing security events of some sort and responding to them by an
 * action
 *
 * @author Lubos Housa
 * @since Nov 8, 2014 11:47:34 PM
 */
public interface Rule {

    /**
     * Process this security event and detects whether this represents a security breach from the perspective of this rule
     * 
     * @param event
     *            event to process
     * @return true if this event represents a security breach, false otherwise
     */
    boolean isSecurityBreach(SecurityEvent event);

    /**
     * Detects whether this rule is applicable in the in-passed security mode. Each rule guarantees to return the very same value of this method at
     * runtime. E.g. if this method returns true for an instance, it is guaranteed it will always return true within the lifecycle of this object
     * 
     * @param securityMode
     *            security mode to test applicability of this rule against
     * @return true if so, false otherwise
     */
    boolean isApplicable(SecurityMode securityMode);

    /**
     * Get human readable description of this rule
     * 
     * @return description of this rule
     */
    String getDescription();
}

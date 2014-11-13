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
     * @return security breach - either a breach or not (could be delayed)
     */
    SecurityBreach isSecurityBreach(SecurityEvent event);

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
     * Detects whether this rule is enabled or not
     * 
     * @return true if so, false otherwise
     */
    boolean isEnabled();

    /**
     * Get human readable description of this rule. It should be a unique string compared to all other rules
     * 
     * @return unique description of this rule
     */
    String getDescription();

    /**
     * Gets Unique ID of this Rule
     * 
     * @return integer identification of this rule
     */
    int getId();

    /**
     * Update this rule based on the in-passed rule detail (e.g. whether it is enabled, etc)
     * 
     * @param ruleDetail
     *            rule detail to use to update this rule
     */
    void updateFrom(RuleDetail ruleDetail);
}

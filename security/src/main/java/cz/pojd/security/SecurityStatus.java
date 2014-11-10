package cz.pojd.security;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.model.web.CalendarEvent;
import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.rules.RuleDetail;

/**
 * Security status is a wrapper for all available information about security at runtime
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 1:07:01 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityStatus {

    private SecurityMode securityMode;
    private Collection<RuleDetail> rules;
    private Collection<CalendarEvent> events;

    public SecurityMode getSecurityMode() {
	return securityMode;
    }

    public void setSecurityMode(SecurityMode securityMode) {
	this.securityMode = securityMode;
    }

    public Collection<RuleDetail> getRules() {
	return rules;
    }

    public void setRules(Collection<RuleDetail> rules) {
	this.rules = rules;
    }

    public Collection<CalendarEvent> getEvents() {
	return events;
    }

    public void setEvents(Collection<CalendarEvent> events) {
	this.events = events;
    }

    @Override
    public String toString() {
	return "SecurityStatus [securityMode=" + securityMode + ", rules=" + rules + ", events=" + events + "]";
    }
}

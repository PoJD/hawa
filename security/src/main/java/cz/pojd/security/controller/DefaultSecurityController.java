package cz.pojd.security.controller;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;

public class DefaultSecurityController implements Controller {
    private static final Log LOG = LogFactory.getLog(DefaultSecurityController.class);

    private SecurityMode securityMode = SecurityMode.FULL_HOUSE;
    private final Set<Rule> fullHouseRules = new LinkedHashSet<>();
    private final Set<Rule> emptyHouseRules = new LinkedHashSet<>();
    private final Set<Rule> rules = new LinkedHashSet<>();
    private Set<SecurityHandler> securityHandlers;

    public DefaultSecurityController(SecurityHandler... securityHandlers) {
	this.securityHandlers = new LinkedHashSet<>();
	for (SecurityHandler securityHandler : securityHandlers) {
	    this.securityHandlers.add(securityHandler);
	}
    }

    public Set<SecurityHandler> getSecurityHandlers() {
	return securityHandlers;
    }

    public void setSecurityHandlers(Set<SecurityHandler> securityHandlers) {
	this.securityHandlers = securityHandlers;
    }

    @Override
    public void handle(SecurityEvent securityEvent) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Security event fired: " + securityEvent);
	}

	try {
	    switch (securityMode) {
	    case FULL_HOUSE:
		fireRules(fullHouseRules, securityEvent);
		break;
	    case EMPTY_HOUSE:
		fireRules(emptyHouseRules, securityEvent);
		break;
	    case OFF:
		LOG.debug("Ignoring security event since the security mode is off");
	    }
	} finally {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Disposing security event: " + securityEvent);
	    }
	    securityEvent.dispose();
	}
    }

    private void fireRules(Set<Rule> rules, SecurityEvent securityEvent) {
	for (Rule rule : rules) {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Processing " + securityEvent + " by rule " + rule);
	    }

	    boolean isSecurityBreach = rule.isSecurityBreach(securityEvent);
	    if (isSecurityBreach) {
		if (LOG.isInfoEnabled()) {
		    LOG.info("Security breach detected: " + securityEvent + ". Rule firing this breach: " + rule + ". Security mode: " + securityMode);
		    LOG.info("Firing security breach handlers and skipping next rules...");
		}
		for (SecurityHandler securityHandler : securityHandlers) {
		    securityHandler.handleSecurityBreach(securityEvent);
		}
		return;
	    }
	}
    }

    @Override
    public void registerRule(Rule rule) {
	LOG.info("Registering rule: " + rule);
	rules.add(rule);
	if (rule.isApplicable(SecurityMode.FULL_HOUSE)) {
	    fullHouseRules.add(rule);
	}
	if (rule.isApplicable(SecurityMode.EMPTY_HOUSE)) {
	    emptyHouseRules.add(rule);
	}
    }

    @Override
    public Set<Rule> getRegisteredRules() {
	return rules;
    }

    @Override
    public void switchMode(SecurityMode newMode) {
	this.securityMode = newMode;
    }
}

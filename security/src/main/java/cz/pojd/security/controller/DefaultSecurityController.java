package cz.pojd.security.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;

public class DefaultSecurityController implements Controller {
    private static final Log LOG = LogFactory.getLog(DefaultSecurityController.class);

    private final List<Rule> fullHouseRules = new ArrayList<>();
    private final List<Rule> emptyHouseRules = new ArrayList<>();
    private final List<Rule> offRules = new ArrayList<>();
    private final List<SecurityHandler> securityHandlers;

    private SecurityMode securityMode = SecurityMode.FULL_HOUSE;

    @Inject
    public DefaultSecurityController(RulesDAO rulesDao, SecurityHandler... securityHandlers) {
	LOG.info("Initating DefaultSecurityController...");
	for (Rule rule : rulesDao.queryAllRules()) {
	    registerRule(rule);
	}
	this.securityHandlers = Arrays.asList(securityHandlers);
	LOG.info("Init done.");
    }

    private void registerRule(Rule rule) {
	LOG.info("Registering rule: " + rule);
	if (rule.isApplicable(SecurityMode.FULL_HOUSE)) {
	    fullHouseRules.add(rule);
	}
	if (rule.isApplicable(SecurityMode.EMPTY_HOUSE)) {
	    emptyHouseRules.add(rule);
	}
	if (rule.isApplicable(SecurityMode.OFF)) { // something is working all the time (e.g. temperature)
	    offRules.add(rule);
	}
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
		fireRules(offRules, securityEvent);
	    }
	} finally {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Disposing security event: " + securityEvent);
	    }
	    securityEvent.dispose();
	}
    }

    private void fireRules(List<Rule> rules, SecurityEvent securityEvent) {
	for (Rule rule : rules) {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Processing " + securityEvent + " by rule " + rule);
	    }

	    if (rule.isEnabled() && rule.isSecurityBreach(securityEvent)) {
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
    public void switchMode(SecurityMode newMode) {
	LOG.info("Switching the securityMode to " + newMode);
	this.securityMode = newMode;
    }

    @Override
    public SecurityMode getMode() {
	return securityMode;
    }
}

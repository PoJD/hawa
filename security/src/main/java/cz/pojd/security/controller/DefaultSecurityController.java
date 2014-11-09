package cz.pojd.security.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;

public class DefaultSecurityController implements Controller {
    private static final Log LOG = LogFactory.getLog(DefaultSecurityController.class);

    private final Map<SecurityMode, List<Rule>> rulesByMode = new HashMap<SecurityMode, List<Rule>>();
    private final List<SecurityHandler> securityHandlers;

    private SecurityMode securityMode = SecurityMode.FULL_HOUSE;
    private RoomsDAO roomsDAO;

    @Inject
    public DefaultSecurityController(RoomsDAO roomsDAO, RulesDAO rulesDao, SecurityHandler... securityHandlers) {
	LOG.info("Initating DefaultSecurityController...");
	this.roomsDAO = roomsDAO;
	for (Rule rule : rulesDao.queryAllRules()) {
	    registerRule(rule);
	}
	this.securityHandlers = Arrays.asList(securityHandlers);
	LOG.info("Init done.");
    }

    private void registerRule(Rule rule) {
	LOG.info("Registering rule: " + rule);
	for (SecurityMode securityMode : SecurityMode.values()) {
	    List<Rule> rules = rulesByMode.get(securityMode);
	    if (rules == null) {
		rules = new ArrayList<>();
		rulesByMode.put(securityMode, rules);
	    }
	    if (rule.isApplicable(securityMode)) {
		rules.add(rule);
	    }
	}
    }

    @Override
    public void handle(SecurityEvent securityEvent) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Security event fired: " + securityEvent);
	}

	try {
	    fireRules(securityEvent);
	} finally {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Disposing security event: " + securityEvent);
	    }
	    securityEvent.dispose();
	}
    }

    private void fireRules(SecurityEvent securityEvent) {
	for (Rule rule : rulesByMode.get(securityMode)) {
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
	
	// TODO does this belong here?
	if (SecurityMode.EMPTY_HOUSE == securityMode) {
	    roomsDAO.switchOffAllLights();
	}
    }

    @Override
    public SecurityMode getMode() {
	return securityMode;
    }
}

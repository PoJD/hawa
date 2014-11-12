package cz.pojd.security.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.sensors.observable.Observable;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;

public class DefaultSecurityController implements Controller {
    private static final Log LOG = LogFactory.getLog(DefaultSecurityController.class);

    private final Map<SecurityMode, List<Rule>> rulesByMode = new HashMap<SecurityMode, List<Rule>>();
    private final List<SecurityHandler> securityHandlers;

    private SecurityMode securityMode = SecurityMode.OFF;
    private Observable<Controller, SecurityMode> observable = new Observable<>();

    @Inject
    public DefaultSecurityController(RulesDAO rulesDao, SecurityHandler... securityHandlers) {
	LOG.info("Initating DefaultSecurityController...");
	for (Rule rule : rulesDao.queryRules()) {
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
		    try {
			securityHandler.handleSecurityBreach(securityEvent);
		    } catch (Exception e) {
			LOG.error("Some error occured while processing security event breach " + securityEvent + " by security handler "
				+ securityHandler, e);
		    }
		}
		return;
	    }
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug(securityEvent + " ignored - no rules fired a security breach.");
	}
    }

    @Override
    public void switchMode(SecurityMode newMode) {
	LOG.info("Switching the securityMode to " + newMode);
	this.securityMode = newMode;

	observable.notifyObservers(this, securityMode);
    }

    @Override
    public SecurityMode getMode() {
	return securityMode;
    }

    @Override
    public void addObserver(Observer<Controller, SecurityMode> observer) {
	observable.addObserver(observer);
    }
}

package cz.pojd.security.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.sensors.observable.Observable;
import cz.pojd.rpi.system.TimeService;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.SecurityEventDAO;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;
import cz.pojd.security.rules.SecurityBreach;

public class DefaultSecurityController implements Controller {
    private static final Log LOG = LogFactory.getLog(DefaultSecurityController.class);

    private final Map<SecurityMode, List<Rule>> rulesByMode = new HashMap<SecurityMode, List<Rule>>();
    private final List<SecurityHandler> securityHandlers;
    private final Set<SecurityEvent> delayedEvents = Collections.newSetFromMap(new ConcurrentHashMap<SecurityEvent, Boolean>());
    private final TimeService timeService;
    private final SecurityEventDAO securityEventDAO;

    private SecurityMode securityMode = SecurityMode.OFF;
    private SecurityEvent currentBreach;
    private Observable<Controller, SecurityMode> observable = new Observable<>();

    @Inject
    public DefaultSecurityController(TimeService timeService, RulesDAO rulesDao, SecurityEventDAO securityEventDAO,
	    SecurityHandler... securityHandlers) {
	LOG.info("Initiating DefaultSecurityController...");
	this.timeService = timeService;
	for (Rule rule : rulesDao.queryRules()) {
	    registerRule(rule);
	}
	this.securityEventDAO = securityEventDAO;
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

    /**
     * Every 2 minutes perform some housekeeping
     */
    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void houseKeeping() {
	handleDelayedEvents();
	handleSecurityBreach();
    }

    /**
     * Check the delayed events
     */
    public void handleDelayedEvents() {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Handle delayed events called.");
	}
	for (Iterator<SecurityEvent> iterator = delayedEvents.iterator(); iterator.hasNext();) {
	    SecurityEvent securityEvent = iterator.next();
	    if (!(timePassed(securityEvent, SecurityBreach.DELAY_MINUTES))) {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Security event " + securityEvent + " is younger than " + SecurityBreach.DELAY_MINUTES
			    + " minutes. Skipping this and following security events for further processing.");
		}
		break;
	    }
	    // otherwise we have an event to process and drop from the set
	    handle(securityEvent, true);
	    iterator.remove();
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Handle delayed events finished. Remaining delayed events: " + delayedEvents);
	}
    }

    /**
     * Check for current security breach
     */
    public void handleSecurityBreach() {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Handle security breach called.");
	}
	SecurityEvent currentBreach = getCurrentBreach();
	if (currentBreach != null) {
	    if (timePassed(currentBreach, SecurityBreach.DURATION_MINUTES)) {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Security breach " + getCurrentBreach() + " is older than " + SecurityBreach.DURATION_MINUTES
			    + " minutes. Dismissing this security breach.");
		}
		dismissBreach();
	    }
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Handle security breach finished.");
	}
    }

    @Override
    public void handle(SecurityEvent securityEvent) {
	handle(securityEvent, false);
    }

    private boolean timePassed(SecurityEvent securityEvent, int minutes) {
	DateTime now = timeService.now();
	DateTime securityEventTime = securityEvent.getAt();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Checking whether the time already passed. Now: " + now + ". Event time: " + securityEventTime + ". Minutes to check: "
		    + minutes);
	}
	return now.minusMinutes(minutes).isAfter(securityEventTime);
    }

    private void handle(SecurityEvent securityEvent, boolean processingDelayedEvents) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Processing security event: " + securityEvent);
	}

	boolean shouldDisposeEvent = true;
	try {
	    shouldDisposeEvent = fireRules(securityEvent, processingDelayedEvents);
	} finally {
	    if (shouldDisposeEvent) {
		securityEvent.dispose();
	    }
	}
    }

    private boolean fireRules(SecurityEvent securityEvent, boolean processingDelayedEvents) {
	for (Rule rule : rulesByMode.get(securityMode)) {
	    if (rule.isEnabled()) {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Processing " + securityEvent + " by rule " + rule);
		}

		SecurityBreach securityBreach = rule.isSecurityBreach(securityEvent);
		if (SecurityBreach.NOW == securityBreach) {
		    return processSecurityBreach(securityEvent, rule);
		} else if (SecurityBreach.DELAYED == securityBreach) {
		    return processDelayedSecurityBreach(securityEvent, rule, processingDelayedEvents);
		}
	    }
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug(securityEvent + " ignored - no rules fired a security breach.");
	}
	return true;
    }

    private boolean processDelayedSecurityBreach(SecurityEvent securityEvent, Rule rule, boolean processingDelayedEvents) {
	if (processingDelayedEvents) { // in this case the event got fired again, so throw it now
	    processSecurityBreach(securityEvent, rule);
	    return true;
	} else { // otherwise store the event for later processing
	    securityEvent.setMinor(true); // delayed are minor by default
	    if (LOG.isInfoEnabled()) {
		LOG.info("Security breach detected: " + securityEvent + ". Rule firing this breach: " + rule + ". Security mode: "
			+ securityMode);
		LOG.info("Since this securityEvent is delayed, storing it for later processing and skipping next rules...");
	    }
	    storeEvent(securityEvent);
	    return false;
	}
    }

    private void storeEvent(SecurityEvent securityEvent) {
	delayedEvents.add(securityEvent);
    }

    private boolean processSecurityBreach(SecurityEvent securityEvent, Rule rule) {
	if (LOG.isInfoEnabled()) {
	    LOG.info("Security breach detected: " + securityEvent + ". Rule firing this breach: " + rule + ". Security mode: "
		    + securityMode);
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
	this.currentBreach = securityEvent;
	return true;
    }

    @Override
    public void switchMode(SecurityMode newMode) {
	LOG.info("Switching the securityMode to " + newMode);
	this.securityMode = newMode;

	observable.notifyObservers(this, securityMode);
	afterModeSwitch();
    }

    private void afterModeSwitch() {
	if (SecurityMode.OFF == securityMode) {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Dropping all delayed events...:  " + delayedEvents);
	    }
	    for (Iterator<SecurityEvent> iterator = delayedEvents.iterator(); iterator.hasNext();) {
		iterator.next().dispose();
		iterator.remove();
	    }
	}
    }

    @Override
    public SecurityMode getMode() {
	return securityMode;
    }

    @Override
    public void addObserver(Observer<Controller, SecurityMode> observer) {
	observable.addObserver(observer);
    }

    @Override
    public SecurityEvent getCurrentBreach() {
	return currentBreach;
    }

    @Override
    public boolean isBreach() {
	return getCurrentBreach() != null;
    }

    @Override
    public void dismissBreach() {
	SecurityEvent currentBreach = this.currentBreach;
	this.currentBreach = null;
	if (currentBreach != null) {
	    String detail = currentBreach.getDetail();
	    currentBreach.setDetail((detail != null ? detail : "") + " - alarm dismissed");
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Storing the dimiss security event: " + currentBreach);
	    }
	    securityEventDAO.save(currentBreach);
	}

    }
}

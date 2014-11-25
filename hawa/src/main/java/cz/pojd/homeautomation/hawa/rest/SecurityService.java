package cz.pojd.homeautomation.hawa.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.rest.parse.DateTimeParam;
import cz.pojd.homeautomation.model.web.CalendarEvent;
import cz.pojd.security.SecurityStatus;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.CalendarEventTranslator;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.SecurityEventDAO;
import cz.pojd.security.rules.RuleDetail;
import cz.pojd.security.rules.RulesDAO;

@Path("/security")
public class SecurityService {
    private static final Log LOG = LogFactory.getLog(SecurityService.class);

    @Inject
    private RulesDAO rulesDAO;
    @Inject
    private SecurityEventDAO securityEventDAO;
    @Inject
    private CalendarEventTranslator translator;
    @Inject
    private Controller securityController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SecurityStatus query() {
	LOG.info("Detecting current state of security...");

	SecurityStatus result = new SecurityStatus();
	result.setRules(rulesDAO.query());
	result.setSecurityMode(securityController.getMode());
	if (LOG.isDebugEnabled()) {
	    LOG.debug("State detected: " + result);
	}

	LOG.info("State detected.");
	return result;
    }

    /**
     * Gets the collection of calendar event via REST
     * 
     * @param start
     *            start date - ISO8601 string format
     * @param end
     *            end date - ISO8601 string format
     * @return calendar event in the specified range of dates
     */
    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CalendarEvent> queryCalendarEvents(@QueryParam("start") DateTimeParam start, @QueryParam("end") DateTimeParam end) {
	LOG.info("Detecting calendar events for time range [" + start + ", " + end + "]");

	Collection<CalendarEvent> result = new ArrayList<>();
	for (SecurityEvent securityEvent : securityEventDAO.query(start.getDateTime(), end.getDateTime())) {
	    result.add(translator.translate(securityEvent));
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Calendar events found: " + result);
	}

	LOG.info("Calendar events found.");
	return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(SecurityStatus status) {
	LOG.info("About to update security based on the status: " + status);
	securityController.switchMode(status.getSecurityMode());
	for (RuleDetail ruleDetail : status.getRules()) {
	    rulesDAO.save(ruleDetail);
	}
	LOG.info("Security updated.");
    }
}

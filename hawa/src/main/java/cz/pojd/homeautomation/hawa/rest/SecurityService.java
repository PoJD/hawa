package cz.pojd.homeautomation.hawa.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.security.SecurityStatus;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.rules.RuleDetail;
import cz.pojd.security.rules.RulesDAO;

@Path("/security")
public class SecurityService {
    private static final Log LOG = LogFactory.getLog(SecurityService.class);

    @Inject
    private RulesDAO rulesDAO;
    @Inject
    private Controller securityController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SecurityStatus query() {
	LOG.info("Detecting current state of security...");

	SecurityStatus result = new SecurityStatus();
	result.setRules(rulesDAO.queryAllRuleDetails());
	result.setSecurityMode(securityController.getMode());
	if (LOG.isDebugEnabled()) {
	    LOG.debug("State detected: " + result);
	}

	LOG.info("State detected.");
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

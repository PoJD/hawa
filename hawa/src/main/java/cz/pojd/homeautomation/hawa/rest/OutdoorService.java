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

import cz.pojd.homeautomation.model.outdoor.OutdoorDAO;
import cz.pojd.homeautomation.model.outdoor.OutdoorDAOException;
import cz.pojd.homeautomation.model.outdoor.OutdoorDetail;

/**
 * OutdoorService detects the current weather conditions and allows to control outdoor gadgets
 * 
 * @author Lubos Housa
 * @since Aug 3, 2014 1:09:28 AM
 */
@Path("/outdoor")
public class OutdoorService {
    private static final Log LOG = LogFactory.getLog(OutdoorService.class);

    @Inject
    private OutdoorDAO outdoorDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public OutdoorDetail get() {
	LOG.info("Detecting current state of outdoor...");
	OutdoorDetail result = outdoorDAO.get();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Detected outdoor: " + result);
	}

	return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/withHistory")
    public OutdoorDetail getWithHistory() {
	LOG.info("Detecting current state of outdoor including the history...");
	OutdoorDetail result = outdoorDAO.getWithHistory();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Outdoor with history detected.");
	}
	if (LOG.isTraceEnabled()) {
	    LOG.trace("Detail: " + result);
	}

	return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(OutdoorDetail outdoorDetail) {
	LOG.info("About to save outdoor: " + outdoorDetail);
	try {
	    outdoorDAO.save(outdoorDetail);
	} catch (OutdoorDAOException e) {
	    LOG.error("Unable to save the outdoorDetail: ", e);
	    throw e;
	}
    }
}

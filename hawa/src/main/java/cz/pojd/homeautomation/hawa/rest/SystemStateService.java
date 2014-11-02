package cz.pojd.homeautomation.hawa.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.refresh.Refresher;
import cz.pojd.rpi.state.OsStateService;
import cz.pojd.rpi.state.PropertyValue;
import cz.pojd.rpi.state.SystemState;
import cz.pojd.rpi.state.VmStateService;

/**
 * SystemStateService is a Restful web service providing information about the running JVM and Operating System
 * 
 * @author Lubos Housa
 * @since Jul 20, 2014 11:44:54 PM
 */
@Path("/systemstate")
public class SystemStateService {

    private static final Log LOG = LogFactory.getLog(SystemStateService.class);

    @Inject
    private VmStateService vmStateService;
    @Inject
    private OsStateService osStateService;
    @Inject
    private Refresher refresher;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SystemState get() {
	LOG.info("Detecting current state of the JVM and the OS...");
	SystemState result = getState();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("System state: " + result);
	}
	return result;
    }

    @GET
    @Path("/withDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public SystemState getWithDetails() {
	LOG.info("Detecting current state of the JVM and the OS and logs...");
	SystemState result = getState();

	result.setLogSystem(osStateService.getSystemLog());
	result.setLogApplication(osStateService.getApplicationLog());

	if (LOG.isTraceEnabled()) {
	    LOG.trace("System state: " + result);
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("System state detected.");
	}
	return result;
    }

    @PUT
    public void shutdown() {
	LOG.info("Shutting down the system...");
	osStateService.shutdown();
    }

    private SystemState getState() {
	SystemState result = new SystemState();
	result.addValue(vmStateService.getHeap());
	result.addValue(vmStateService.getProcessors());
	result.addValue(osStateService.getCpu());
	result.addValue(osStateService.getRam());
	result.addValue(osStateService.getSwap());
	for (PropertyValue value : osStateService.getFileSystems()) {
	    result.addValue(value);
	}
	result.setLastUpdate(refresher.getLastUpdate());
	result.setDbRunning(osStateService.isDbRunning());
	return result;
    }
}

package cz.pojd.homeautomation.hawa.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.refresh.Refresher;
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
    public SystemState getSystemState() {
	LOG.info("Detecting current state of the JVM and the OS...");
	SystemState result = new SystemState(refresher.getLastUpdate());
	result.addValue(vmStateService.getHeap());
	result.addValue(vmStateService.getProcessors());
	result.addValue(osStateService.getCpu());
	result.addValue(osStateService.getRam());
	result.addValue(osStateService.getSwap());
	for (PropertyValue value : osStateService.getFileSystems()) {
	    result.addValue(value);
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("System state: " + result);
	}
	return result;
    }
}

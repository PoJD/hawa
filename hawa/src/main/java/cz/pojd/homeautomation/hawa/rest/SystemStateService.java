package cz.pojd.homeautomation.hawa.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.state.OsStateService;
import cz.pojd.rpi.state.PropertyValue;
import cz.pojd.rpi.state.VmStateService;

/**
 * SystemStateService is a Restful web service providing information about the
 * running JVM and Operating System
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PropertyValue> getAll() {
	LOG.info("Detecting current state of the JVM and the OS...");
	List<PropertyValue> result = new ArrayList<>();
	saveAdd(result, vmStateService.getHeap());
	saveAdd(result, vmStateService.getProcessors());
	saveAdd(result, osStateService.getCpu());
	saveAdd(result, osStateService.getRam());
	saveAdd(result, osStateService.getSwap());
	for (PropertyValue value : osStateService.getFileSystems()) {
	    saveAdd(result, value);
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("List of properties: " + result);
	}
	return result;
    }

    private void saveAdd(List<PropertyValue> result, PropertyValue value) {
	if (value != null) {
	    result.add(value);
	}
    }
}

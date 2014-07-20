package cz.pojd.homeautomation.hawa.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cz.pojd.homeautomation.hawa.rest.VmStateProperty.Type;

/**
 * VmStateService is a Restful web service providing information about the running JVM.
 *
 * @author Lubos Housa
 * @since Jul 20, 2014 11:44:54 PM
 */
@Path("/vmstate")
public class VmStateService {

    private static final long MB = 1024l * 1024l;
    private static final Logger LOG = Logger.getLogger(VmStateService.class.getName());
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VmStateProperty> getAll() {
	LOG.info("Getting all properties of VmState");
	List<VmStateProperty> result = new ArrayList<>();
	result.add(detectHeap());
	result.add(detectProcessors());
	if (LOG.isLoggable(Level.FINE)) {
	    LOG.fine("Actual state of VM: " + result);
	}
	return result;
    }

    private VmStateProperty detectProcessors() {
	int processorsCount = Runtime.getRuntime().availableProcessors();
	return new VmStateProperty(Type.processors, processorsCount, processorsCount, processorsCount + " processors");
    }

    private VmStateProperty detectHeap() {
	long max = Runtime.getRuntime().totalMemory();
	long used = max - Runtime.getRuntime().freeMemory();
	return new VmStateProperty(Type.heap, used, max, longtoStringInMB(used) + " / " + longtoStringInMB(max));
    }
    
    private String longtoStringInMB(long size) {
	return (size/MB) + "MB";
    }
}

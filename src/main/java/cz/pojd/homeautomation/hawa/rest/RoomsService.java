package cz.pojd.homeautomation.hawa.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.rest.rooms.Room;
import cz.pojd.homeautomation.hawa.rest.rooms.RoomsDAO;

@Path("/rooms")
public class RoomsService {
    private static final Log LOG = LogFactory.getLog(RoomsService.class);

    @Inject
    private RoomsDAO roomsDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAll() {
	LOG.info("Detecting current state of all rooms...");
	List<Room> result = roomsDAO.getAll();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("List of rooms: " + result);
	}

	return result;
    }
}

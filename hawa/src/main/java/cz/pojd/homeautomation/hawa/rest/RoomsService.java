package cz.pojd.homeautomation.hawa.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.rooms.RoomState;
import cz.pojd.homeautomation.hawa.rooms.RoomsDAO;
import cz.pojd.homeautomation.hawa.rooms.RoomsDAOException;

@Path("/rooms")
public class RoomsService {
    private static final Log LOG = LogFactory.getLog(RoomsService.class);

    @Inject
    private RoomsDAO roomsDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomState> getAll() {
	LOG.info("Detecting current state of all rooms...");
	List<RoomState> result = roomsDAO.getAll();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("List of rooms: " + result);
	}

	return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveRoom(RoomState room) {
	LOG.info("About to save room: " + room);
	try {
	    roomsDAO.save(room);
	} catch (RoomsDAOException e) {
	    LOG.error("Unable to save the room: ", e);
	    throw e;
	}
    }
}

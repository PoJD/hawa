package cz.pojd.homeautomation.hawa.rest;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.rooms.RoomDetail;
import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.homeautomation.model.rooms.RoomsDAOException;

@Path("/rooms")
public class RoomsService {
    private static final Log LOG = LogFactory.getLog(RoomsService.class);

    @Inject
    private RoomsDAO roomsDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<RoomDetail> query() {
	LOG.info("Detecting current state of all rooms...");
	Collection<RoomDetail> result = roomsDAO.query();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("List of rooms: " + result);
	}
	return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{roomName}")
    public RoomDetail get(@PathParam("roomName") String roomName) {
	LOG.info("Detecting state of room: " + roomName);
	RoomDetail result = roomsDAO.get(roomName);
	if (LOG.isTraceEnabled()) {
	    LOG.trace("Detail of the room: " + result);
	}
	return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(RoomDetail room) {
	LOG.info("About to save room: " + room);
	try {
	    roomsDAO.save(room);
	} catch (RoomsDAOException e) {
	    LOG.error("Unable to save the room: ", e);
	    throw e;
	}
    }

    @POST
    @Path("/control/switchOffLights")
    public void switchOffAllLights() {
	LOG.info("About to switch of all lights.");
	roomsDAO.switchOffAllLights();
    }
}

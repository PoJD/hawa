package cz.pojd.homeautomation.hawa.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.liveview.LiveView;
import cz.pojd.rpi.controls.CameraControl;

/**
 * LiveViewService allows starting live view streaming to the client if requested.
 * 
 * @author Lubos Housa
 * @since Sep 1, 2014 11:39:24 PM
 */
@Path("/liveview")
public class LiveViewService {

    private static final Log LOG = LogFactory.getLog(LiveViewService.class);

    @Inject
    private CameraControl cameraControl;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public LiveView get() {
	LOG.info("Starting up the camera or sending heartbeat if already running...");
	return new LiveView(cameraControl.startOrPing());
    }
}

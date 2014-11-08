package cz.pojd.security.motion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.lights.LightCapable;
import cz.pojd.homeautomation.model.outdoor.OutdoorDAO;
import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.homeautomation.model.spring.RoomSpecification;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.Source;

/**
 * Trigger for motion sensors - is responsible for making sure the motion sensors are hooked up to the security system
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 12:00:29 AM
 */
public class MotionSensorSecurityTrigger {
    private static final Log LOG = LogFactory.getLog(MotionSensorSecurityTrigger.class);

    public MotionSensorSecurityTrigger(RoomsDAO roomsDAO, OutdoorDAO outdoorDAO, Controller controller) {
	LOG.info("Hooking up motion sensors to the security system...");

	setupMotionSensor(roomsDAO.getRoom(RoomSpecification.HALL_DOWN), Source.HALL_DOWN, controller);
	setupMotionSensor(roomsDAO.getRoom(RoomSpecification.HALL_UP), Source.HALL_UP, controller);
	setupMotionSensor(outdoorDAO.getOutdoor(), Source.OUTDOOR, controller);

	LOG.info("Done");
    }

    private void setupMotionSensor(LightCapable lightCapable, Source source, Controller controller) {
	if (lightCapable.getMotionSensor() != null) {
	    LOG.info("Hooking up " + source + " ...");
	    lightCapable.getMotionSensor().addObserver(new MotionSensorSecurityObserver(source, controller));
	}
    }
}

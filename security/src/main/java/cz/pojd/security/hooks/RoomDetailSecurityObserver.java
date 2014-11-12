package cz.pojd.security.hooks;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.rooms.RoomDetail;
import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;

/**
 * Observer for hooking up roomsDAO to the security system - reads new room details anytime they are updated and raises a security event if the
 * temperature is above a threshold.
 *
 * @author Lubos Housa
 * @since Nov 12, 2014 6:20:39 PM
 */
public class RoomDetailSecurityObserver implements Observer<RoomsDAO, RoomDetail> {
    private static final Log LOG = LogFactory.getLog(RoomDetailSecurityObserver.class);

    private final Controller securityController;
    private final int threshold;

    /**
     * Create new instance of this observer.
     * 
     * @param securityController
     *            security controller to hookup to
     * @param threshold
     *            threshold in Celsius degrees for temperature - anything higher than this detected anywhere would fire a security event
     */
    @Inject
    public RoomDetailSecurityObserver(Controller securityController, int threshold) {
	this.securityController = securityController;
	this.threshold = threshold;
    }

    @Override
    public void update(RoomsDAO source, RoomDetail newValue) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Checking room detail " + newValue + " - whether the temperature is not above threshold: " + threshold);
	}
	if (newValue.getTemperature() != null && newValue.getTemperature().getDoubleValue() > threshold) {
	    LOG.info("Temperature above threshold detected. Room: " + newValue + ", temperature: " + newValue.getTemperature().getDoubleValue()
		    + ", threshold: " + threshold);
	    SecurityEvent event = new SecurityEvent();
	    event.setType(Type.highTemperature);
	    event.setSource(newValue.getSpecification());
	    securityController.handle(event);
	}
    }
}

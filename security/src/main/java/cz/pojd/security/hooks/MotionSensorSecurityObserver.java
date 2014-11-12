package cz.pojd.security.hooks;

import cz.pojd.homeautomation.model.Source;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.Controllable;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;

/**
 * Special observer simply notifying the security controller anytime the associated motion sensor detects a move
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 12:17:53 AM
 */
public class MotionSensorSecurityObserver implements Observer<Controllable, Boolean> {

    private final Source source;
    private final Controller securityController;

    public MotionSensorSecurityObserver(Source source, Controller securityController) {
	this.source = source;
	this.securityController = securityController;
    }

    @Override
    public void update(Controllable controllable, Boolean switchedOn) {
	if (switchedOn) {
	    SecurityEvent event = new SecurityEvent();
	    event.setType(Type.sensorMotionDetected);
	    event.setSource(source);

	    securityController.handle(event);
	}
    }
}

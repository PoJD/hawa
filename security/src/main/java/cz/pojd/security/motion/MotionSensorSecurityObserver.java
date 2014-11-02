package cz.pojd.security.motion;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.sensors.observable.ObservableSensor;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;

/**
 * Special observer simply notifying the security controller anytime the associated motion sensor detects a move
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 12:17:53 AM
 */
public class MotionSensorSecurityObserver implements Observer {

    private final String where;
    private final Controller securityController;

    public MotionSensorSecurityObserver(String where, Controller securityController) {
	this.where = where;
	this.securityController = securityController;
    }

    @Override
    public void switched(ObservableSensor source, boolean switchedOn) {
	if (switchedOn) {
	    SecurityEvent event = new SecurityEvent();
	    event.setType(Type.sensorMotionDetected);
	    event.setWhere(where);

	    securityController.handle(event);
	}
    }
}

package cz.pojd.security.hooks;

import cz.pojd.homeautomation.model.Source;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.Controllable;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;

/**
 * Special observer simply notifying the security controller anytime the associated sensor detects a change
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 12:17:53 AM
 */
public class SensorSecurityObserver implements Observer<Controllable, Boolean> {

    private final Source source;
    private final Controller securityController;
    private final Type type;
    private final boolean switchedOnRaised;

    /**
     * Creates new instnace of this observer
     * 
     * @param source
     *            source of the potential security events raised
     * @param securityController
     *            security controller to notify if a breach is detected
     * @param type
     *            type of the event to raise
     * @param switchedOnRaised
     *            true if switchedOn should notify the controller, false if switchedOff should notify the controller
     */
    public SensorSecurityObserver(Source source, Controller securityController, Type type, boolean switchedOnRaised) {
	this.source = source;
	this.securityController = securityController;
	this.type = type;
	this.switchedOnRaised = switchedOnRaised;
    }

    @Override
    public void update(Controllable controllable, Boolean switchedOn) {
	if (switchedOn == switchedOnRaised) {
	    SecurityEvent event = new SecurityEvent();
	    event.setType(type);
	    event.setSource(source);

	    securityController.handle(event);
	}
    }
}

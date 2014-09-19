package cz.pojd.homeautomation.hawa.lights;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Observer simply updating the light capable detail from the light capable instance each time it is invoked.
 *
 * @author Lubos Housa
 * @since Sep 19, 2014 11:16:05 AM
 */
public class LightCapableUpdatingObserver implements Observer {

    private final LightCapable lightCapable;

    public LightCapableUpdatingObserver(LightCapable lightCapable) {
	this.lightCapable = lightCapable;
    }

    @Override
    public void switched(ObservableSensor sensor, boolean switchedOn) {
	if (lightCapable.getLastDetail() != null) {
	    lightCapable.getLastDetail().resetFrom(lightCapable);
	}
    }
}

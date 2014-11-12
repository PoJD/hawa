package cz.pojd.homeautomation.model.lights;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.Controllable;

/**
 * Observer simply updating the light capable detail from the light capable instance each time it is invoked.
 *
 * @author Lubos Housa
 * @since Sep 19, 2014 11:16:05 AM
 */
public class LightCapableUpdatingObserver implements Observer<Controllable, Boolean> {

    private final LightCapable lightCapable;

    public LightCapableUpdatingObserver(LightCapable lightCapable) {
	this.lightCapable = lightCapable;
    }

    @Override
    public void update(Controllable controllable, Boolean switchedOn) {
	if (lightCapable.getLastDetail() != null) {
	    lightCapable.getLastDetail().resetFrom(lightCapable);
	}
    }
}

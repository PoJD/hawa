package cz.pojd.homeautomation.hawa.lights;

import java.util.Observable;
import java.util.Observer;

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
    public void update(Observable o, Object arg) {
	if (lightCapable.getLastDetail() != null) {
	    lightCapable.getLastDetail().resetFrom(lightCapable);
	}
    }
}

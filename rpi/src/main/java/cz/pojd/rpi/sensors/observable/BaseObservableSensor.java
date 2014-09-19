package cz.pojd.rpi.sensors.observable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.BaseControl;

/**
 * ObservableSensor base implementation. Notifies the observers exactly in the order they where added to this observable sensor
 *
 * @author Lubos Housa
 * @since Sep 13, 2014 9:30:33 PM
 */
public abstract class BaseObservableSensor extends BaseControl implements ObservableSensor {
    private static final Log LOG = LogFactory.getLog(BaseObservableSensor.class);

    /*
     * Use our own observer list, avoid using java.util.Observable since: 1) it is following reverse order when notifying (did not want to rely on
     * that implementation detail) 2) we don't need that complexity anyway
     */
    private List<Observer> observers = new ArrayList<>();

    @Override
    public boolean isSwitchedOn() {
	return read().getBooleanValue();
    }

    @Override
    public boolean isSwitchable() {
	return false;
    }

    @Override
    public boolean toggleSwitch() {
	return false;
    }

    @Override
    public boolean switchOn() {
	return false;
    }

    @Override
    public boolean switchOff() {
	return false;
    }

    @Override
    public synchronized void addObserver(Observer o) {
	observers.add(o);
    }

    @Override
    public void notifyObservers(boolean switchedOn) {
	if (!isEnabled()) {
	    if (LOG.isDebugEnabled()) {
		LOG.debug(this + " is not enabled. Therefore no notify fired to observers with switchedOn: " + switchedOn);
	    }
	} else {
	    for (Observer observer : observers) {
		observer.switched(this, switchedOn);
	    }
	}
    }
}

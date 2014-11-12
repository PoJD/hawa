package cz.pojd.rpi.sensors.observable;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.BaseControl;
import cz.pojd.rpi.controls.Controllable;

/**
 * ObservableSensor base implementation. Notifies the observers exactly in the order they where added to this observable sensor
 *
 * @author Lubos Housa
 * @since Sep 13, 2014 9:30:33 PM
 */
public abstract class BaseObservableSensor extends BaseControl implements ObservableSensor {

    private Observable<Controllable, Boolean> observable = new Observable<>();

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
    public synchronized void addObserver(Observer<Controllable, Boolean> o) {
	observable.addObserver(o);
    }

    /**
     * Notify the observers about a change - switched on or off
     * 
     * @param switchedOn
     *            true if the sensor just switched on, false if it just switched off
     */
    public void notifyObservers(boolean switchedOn) {
	observable.notifyObservers(this, switchedOn);
    }
}

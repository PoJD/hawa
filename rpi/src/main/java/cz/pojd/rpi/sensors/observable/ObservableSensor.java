package cz.pojd.rpi.sensors.observable;

import java.util.Observable;
import java.util.Observer;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.BaseControl;
import cz.pojd.rpi.controls.Controllable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Special kind of sensor allowing adding observers to it and letting them get notified any time the reading of the sensor changes.
 * 
 * Is is advised this is used for some binary sensors (e.g. switches or motion sensors). Using for generic sensors (e.g. temperature) is allowed too,
 * but care should be taken when setting up the triggering logic, so that the sensor is not polled too often and too much load is given to the Rpi.
 * Binary switched have direct support tin P4J, so this is the preferred way.
 * 
 * Note that this is in fact also a control - allows to be enabled/disabled, etc.
 *
 * @author Lubos Housa
 * @since Sep 13, 2014 6:20:56 PM
 */
public abstract class ObservableSensor extends BaseControl implements Sensor, Controllable {

    private Observable observable = new Observable() {
	@Override
	public void notifyObservers(Object arg) {
	    setChanged();
	    super.notifyObservers(arg);
	}
    };

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

    /**
     * Add new observer to be notified any time this sensor reading is changed. When that heppens, the Observer's update method is invoked with
     * PinState as the argument (the new value of the sensor)
     * 
     * @param o
     *            observer to observe the changes
     */
    public void addObserver(Observer o) {
	observable.addObserver(o);
    }

    /**
     * Notify the observers about a change - new pinState of this observable sensors
     * 
     * @param pinState
     *            new value of this sensor' state
     */
    protected void notifyObservers(PinState pinState) {
	observable.notifyObservers(pinState);
    }
}

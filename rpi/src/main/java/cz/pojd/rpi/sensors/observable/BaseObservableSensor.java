package cz.pojd.rpi.sensors.observable;

import java.util.Observable;
import java.util.Observer;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.BaseControl;

/**
 * ObservableSensor base implementation
 *
 * @author Lubos Housa
 * @since Sep 13, 2014 9:30:33 PM
 */
public abstract class BaseObservableSensor extends BaseControl implements ObservableSensor {

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

    @Override
    public void addObserver(Observer o) {
	observable.addObserver(o);
    }

    @Override
    public void notifyObservers(PinState pinState) {
	observable.notifyObservers(pinState);
    }
}

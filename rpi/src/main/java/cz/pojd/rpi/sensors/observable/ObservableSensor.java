package cz.pojd.rpi.sensors.observable;

import java.util.Observable;
import java.util.Observer;

import cz.pojd.rpi.controls.Controllable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Special kind of sensor allowing adding observers to it and letting them get notified any time the reading of the sensor changes. Implementations of
 * this interface guarantee that they are immutable (at least from the hashCode/equals perspective) and can therefore be safely used as keys into Maps
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:13:47 AM
 */
public abstract class ObservableSensor extends Observable implements Sensor, Controllable {

    private boolean enabled = true;

    @Override
    public void disable() {
	enabled = false;
    }

    @Override
    public void enable() {
	enabled = true;
    }

    @Override
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }

    /**
     * Add new observer to be notified any time this sensor reading is changed. When that heppens, the Observer's update method is invoked with
     * PinState as the argument (the new value of the sensor)
     * 
     * @param o
     *            observer to observe the changes
     */
    public final void addObserver(Observer o) {
	super.addObserver(o);
    }
}

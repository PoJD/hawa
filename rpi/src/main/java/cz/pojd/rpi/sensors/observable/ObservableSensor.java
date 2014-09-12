package cz.pojd.rpi.sensors.observable;

import java.util.Observable;
import java.util.Observer;

import cz.pojd.rpi.State;
import cz.pojd.rpi.controls.Controllable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Special kind of sensor allowing adding observers to it and letting them get notified any time the reading of the sensor changes. Implementations of
 * this interface guarantee that they are immutable (at least from the hashCode/equals perspective) and can therefore be safely used as keys into
 * Maps.
 * 
 * Is is advised this is used for some binary sensors (eg. switches or motion sensors). Using for generic sensors (e.g. temperature) is allowed too,
 * but might add too much pressure on the runtime since the readings could actually take a long time to compute, thus puting the Rpi under potentially
 * heavy load
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

    /**
     * Returns this observable sensor's state - e.g. whether it is enabled and switched on or not
     * 
     * @return wrapper for this sensor state
     */
    public State getState() {
	return State.newBuilder().initiated(isInitiated()).enabled(isEnabled()).on(read().getBooleanValue()).switchable(false).build();
    }

    /**
     * Resets this sensor from the state
     * 
     * @param state
     *            state to base new state of this sensor on
     */
    public void resetFrom(State state) {
	if (state != null) {
	    setEnabled(state.isEnabled());
	}
    }
}

package cz.pojd.rpi.sensors.observable;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.Control;
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
public interface ObservableSensor extends Sensor, Control {

    /**
     * Add new observer to be notified any time this sensor reading is changed. When that heppens, the Observer's update method is invoked with
     * PinState as the argument (the new value of the sensor)
     * 
     * @param o
     *            observer to observe the changes
     */
    void addObserver(Observer<Controllable, Boolean> o);
}

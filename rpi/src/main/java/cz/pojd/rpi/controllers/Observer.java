package cz.pojd.rpi.controllers;

import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Custom observer pattern used for observing events in controllable instances (typically sensors)
 *
 * @author Lubos Housa
 * @since Sep 19, 2014 9:09:46 PM
 */
public interface Observer {

    /**
     * 
     * Notify the observer that the in passed source just changed to "switch on" or "switch off" based on the other flag
     * 
     * @param source
     *            source where the event occurred
     * @param switchedOn
     *            true if switched on, false otherwise
     */
    public void switched(ObservableSensor source, boolean switchedOn);
}

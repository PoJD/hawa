package cz.pojd.rpi.controllers;

/**
 * Custom observer pattern used for observing events in the application (typically sensors)
 *
 * @author Lubos Housa
 * @since Sep 19, 2014 9:09:46 PM
 */
public interface Observer<S, V> {

    /**
     * Notify the observer that the in passed source just updated. The meaning of that update is driven by the type of the value
     * 
     * @param source
     *            source where the event occurred
     * @param newValue
     *            new value that was observed
     */
    void update(S source, V newValue);
}

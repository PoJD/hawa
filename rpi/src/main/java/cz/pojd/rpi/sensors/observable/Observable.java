package cz.pojd.rpi.sensors.observable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controllers.Observer;

/**
 * Custom observable for a specific V value type (V is fired any time observers are observed and S is the source of that event)
 *
 * @author Lubos Housa
 * @since Nov 12, 2014 5:42:04 PM
 */
public class Observable<S, V> {
    private static final Log LOG = LogFactory.getLog(Observable.class);

    /*
     * Use our own observer list, avoid using java.util.Observable since: 1) it is following reverse order when notifying (did not want to rely on
     * that implementation detail) 2) we don't need that complexity anyway
     */
    private List<Observer<S, V>> observers = new ArrayList<>();

    public synchronized void addObserver(Observer<S, V> observer) {
	observers.add(observer);
    }

    /**
     * 
     * Notify the observers about a change - using the newValue
     * 
     * @param source
     *            the source of the event. May be null, depends on the context of usage and whether it is required or not by the application
     * @param newValue
     *            new value to raise
     */
    public void notifyObservers(S source, V newValue) {
	// always notify observers (even if controllable disabled)
	for (Observer<S, V> observer : observers) {
	    try {
		observer.update(source, newValue);
	    } catch (Exception e) {
		LOG.warn("Observer " + observer + " threw an error when processing newValue " + newValue + " raised in source " + source
			+ ". Skipping and jumping to next observer.", e);
	    }
	}
    }
}

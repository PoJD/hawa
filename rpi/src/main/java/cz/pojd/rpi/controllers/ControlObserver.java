package cz.pojd.rpi.controllers;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * ControlObserver is a special observer switching on the control anytime this observer is notified
 *
 * @author Lubos Housa
 * @since Sep 9, 2014 7:50:49 PM
 */
public class ControlObserver implements Observer {

    private final Control control;

    public ControlObserver(Control control) {
	this.control = control;
    }

    public Control getControl() {
	return control;
    }

    @Override
    public void switched(ObservableSensor sensor, boolean switchedOn) {
	getControl().setSwitchedOn(switchedOn);
    }
}

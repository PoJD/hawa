package cz.pojd.rpi.controllers;

import java.util.Observable;

import cz.pojd.rpi.controls.Control;

/**
 * ObservableControl holds the pair - observable and a control. Anytime the observable fires, the control will be changed accordingly.
 *
 * @author Lubos Housa
 * @since Sep 9, 2014 7:50:49 PM
 */
public class ObservableControl {
    private final Observable observable;
    private final Control control;

    public ObservableControl(Observable observable, Control control) {
	this.observable = observable;
	this.control = control;
    }

    public Observable getObservable() {
	return observable;
    }

    public Control getControl() {
	return control;
    }
}

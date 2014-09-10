package cz.pojd.rpi.controllers;

import java.util.Observable;
import java.util.Observer;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.Control;

/**
 * ControlObserver observing and anytime observable fires, the control will be changed accordingly.
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
    public void update(Observable o, Object arg) {
	PinState state = (PinState) arg;
	if (state.isHigh()) {
	    getControl().switchOn();
	} else {
	    getControl().switchOff();
	}
    }
}

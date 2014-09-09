package cz.pojd.rpi.controllers;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.Control;

/**
 * Controller for observing and firing controls - listens for changes in the observables inside pairs and fires the associated controls
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:43:37 AM
 */
public class ObservableController {

    /**
     * Create new instance of MotionSensorController.
     * 
     * @param controls
     *            pairs of observer and control to create controller for. Each observable in the pair will trigger the control to switch on
     */
    @Inject
    public ObservableController(ObservableControl... controls) {
	for (ObservableControl pair : controls) {
	    final Control control = pair.getControl();
	    pair.getObservable().addObserver(new Observer() {
		@Override
		public void update(Observable o, Object arg) {
		    PinState state = (PinState) arg;
		    if (state.isHigh()) {
			control.switchOn();
		    } else {
			control.switchOff();
		    }
		}
	    });
	}
    }
}

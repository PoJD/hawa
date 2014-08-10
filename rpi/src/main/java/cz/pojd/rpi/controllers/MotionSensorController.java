package cz.pojd.rpi.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Controller for motion sensor changes - listening for those and acting accordingly
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:43:37 AM
 */
public class MotionSensorController implements Observer {

    private static final Log LOG = LogFactory.getLog(MotionSensorController.class);

    private final Map<ObservableSensor, Control> controls;

    @Inject
    public MotionSensorController(ObservableSensor outdoorMotionSensor, Control outdoorLightControl) {
	outdoorMotionSensor.addObserver(this);
	this.controls = new HashMap<>();
	this.controls.put(outdoorMotionSensor, outdoorLightControl);
    }

    @Override
    public void update(Observable o, Object arg) {
	PinState state = (PinState) arg;
	Control control = this.controls.get(o);
	if (control != null) {
	    if (state.isHigh()) {
		control.switchOn();
	    } else {
		control.switchOff();
	    }
	} else {
	    LOG.warn("Unable to find the control for the observable: " + o + ". Ignoring the pinState " + state);
	}
    }
}

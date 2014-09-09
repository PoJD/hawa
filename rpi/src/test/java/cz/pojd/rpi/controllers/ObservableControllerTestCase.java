package cz.pojd.rpi.controllers;

import java.util.Observable;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.Control;

public class ObservableControllerTestCase {

    private Observable sensor;
    private @Mocked Control control;

    @Before
    public void setup() {
	sensor = new Observable() {

	    @Override
	    public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	    }
	};
	new ObservableController(new ObservableControl(sensor, control));
    }

    @Test
    public void testUpdateLightControlSwitchedOn() {
	new NonStrictExpectations() {
	    {
		control.switchOn();
		times = 1;
	    }
	};
	sensor.notifyObservers(PinState.HIGH);
    }

    @Test
    public void testUpdateLightControlSwitchedOff() {
	new NonStrictExpectations() {
	    {
		control.switchOff();
		times = 1;
	    }
	};
	sensor.notifyObservers(PinState.LOW);
    }
}

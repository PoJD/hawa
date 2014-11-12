package cz.pojd.rpi.controllers;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import cz.pojd.rpi.MockObservableSensor;
import cz.pojd.rpi.controls.Control;

public class ControlObserverTestCase {

    private MockObservableSensor sensor;
    private @Mocked Control control;

    @Before
    public void setup() {
	sensor = new MockObservableSensor();
	sensor.addObserver(new ControlObserver(control));
    }

    @Test
    public void testUpdateLightControlSwitchedOn() {
	new NonStrictExpectations() {
	    {
		control.setSwitchedOn(true);
		times = 1;
	    }
	};
	sensor.notifyObservers(true);
    }

    @Test
    public void testUpdateLightControlSwitchedOff() {
	new NonStrictExpectations() {
	    {
		control.setSwitchedOn(false);
		times = 1;
	    }
	};
	sensor.notifyObservers(false);
    }
}

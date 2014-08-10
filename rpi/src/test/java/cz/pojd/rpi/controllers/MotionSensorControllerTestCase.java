package cz.pojd.rpi.controllers;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

public class MotionSensorControllerTestCase {

    private @Mocked
    ObservableSensor sensor;
    private @Mocked
    Control control;

    private MotionSensorController controller;

    @Before
    public void setup() {
	controller = new MotionSensorController(sensor, control);
    }

    @Test
    public void testUpdateLightControlSwitchedOn() {
	new NonStrictExpectations() {
	    {
		control.switchOn();
		times = 1;
	    }
	};
	controller.update(sensor, PinState.HIGH);
    }

    @Test
    public void testUpdateLightControlSwitchedOff() {
	new NonStrictExpectations() {
	    {
		control.switchOff();
		times = 1;
	    }
	};
	controller.update(sensor, PinState.LOW);
    }
}

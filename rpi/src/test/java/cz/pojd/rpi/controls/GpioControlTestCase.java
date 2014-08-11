package cz.pojd.rpi.controls;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Test;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.rpi.sensors.gpio.Gpio;

public class GpioControlTestCase {

    @Mocked
    private Gpio gpio;
    @Mocked
    private GpioPinDigitalOutput output;

    private GpioControl control;
    private Pin pin = RaspiPin.GPIO_00;

    @Test
    public void testMockGpioToggleSwitchNoFail() {
	control = new GpioControl(gpio, "testControl", pin);
	try {
	    control.toggleSwitch();
	} catch (Throwable t) {
	    fail("No error expected here");
	}
    }

    @Test
    public void testMockGpioSwitchOnNoFail() {
	control = new GpioControl(gpio, "testControl", pin);
	try {
	    control.switchOn();
	} catch (Throwable t) {
	    fail("No error expected here");
	}
    }

    @Test
    public void testMockGpioSwitchOffNoFail() {
	control = new GpioControl(gpio, "testControl", pin);
	try {
	    control.switchOff();
	} catch (Throwable t) {
	    fail("No error expected here");
	}
    }

    @Test
    public void testRealGpioToggleSwitchInvokesGpio() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalOutputPin(withEqual(pin));
		result = output;

		output.toggle();
		times = 1;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	control.toggleSwitch();
	assertTrue(control.isEnabled());
    }

    @Test
    public void testRealGpioSwitchOnInvokesGpio() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalOutputPin(withEqual(pin));
		result = output;

		output.high();
		times = 1;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	control.switchOn();
	assertTrue(control.isEnabled());
    }

    @Test
    public void testRealGpioSwitchOffInvokesGpio() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalOutputPin(withEqual(pin));
		result = output;

		output.low();
		times = 1;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	control.switchOff();
	assertTrue(control.isEnabled());
    }

    @Test
    public void testRealGpioDisabledNoGpioInvocation() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalOutputPin(withEqual(pin));
		result = output;

		// regardless what we do, it is switched off once ...
		output.low();
		times = 1;
		output.high();
		maxTimes = 0;
		output.toggle();
		maxTimes = 0;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	control.disable();
	control.switchOn();
	control.switchOff();
	control.switchOff();
	control.switchOff();
	control.toggleSwitch();
	
	assertFalse(control.isEnabled());
    }
}

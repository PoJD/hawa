package cz.pojd.rpi.controls;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Test;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.rpi.sensors.gpio.Gpio;

public class GpioControlTestCase {

    @Mocked
    private Gpio gpio;
    @Mocked
    private GpioPinDigitalMultipurpose output;

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
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
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
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
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
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
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
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
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

    @Test
    public void testEnabledDefaultControlIsSwitchedOff() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
		result = output;

		output.setMode(withEqual(PinMode.DIGITAL_INPUT));
		times = 1;

		output.isHigh();
		result = false;

		output.setMode(withEqual(PinMode.DIGITAL_OUTPUT));
		times = 1;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	assertFalse(control.isSwitchedOn());
    }

    @Test
    public void testEnabledSwitchedOnControlIsSwitchedOn() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
		result = output;

		output.high();
		times = 1;

		output.setMode(withEqual(PinMode.DIGITAL_INPUT));
		times = 1;

		output.isHigh();
		result = true;

		output.setMode(withEqual(PinMode.DIGITAL_OUTPUT));
		times = 1;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	control.switchOn();
	assertTrue(control.isSwitchedOn());
    }

    @Test(expected = RuntimeException.class)
    public void testSwitchedOnReturnstoOutputEvenIfExceptionIsThrown() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalMultipurposePin(withEqual(pin), withEqual(PinMode.DIGITAL_OUTPUT));
		result = output;

		output.setMode(withEqual(PinMode.DIGITAL_INPUT));
		times = 1;

		output.isHigh();
		result = new RuntimeException("Some bad GPIO error here ");

		output.setMode(withEqual(PinMode.DIGITAL_OUTPUT));
		times = 1;
	    }
	};
	control = new GpioControl(gpio, "testControl", pin);
	control.switchOn();
	control.isSwitchedOn(); // this should throw the exception above
    }
}

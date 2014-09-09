package cz.pojd.rpi.controls;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;

import cz.pojd.rpi.sensors.gpio.Gpio;

/**
 * Control implementation using raw GPIO access (via P4J)
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:56:10 AM
 */
public class GpioControl extends BaseControl implements Control {

    private static final Log LOG = LogFactory.getLog(GpioControl.class);

    private final GpioPinDigitalMultipurpose gpioPin;
    private final String name;
    private final Runnable low, high, toggle;

    @Inject
    public GpioControl(Gpio gpio, String name, Pin pin) {
	this(gpio, gpio.getDefaultProvider(), name, pin);
    }

    @Inject
    public GpioControl(Gpio gpio, GpioProvider provider, String name, Pin pin) {
	LOG.info("Creating GpioControl '" + name + "' connected to pin " + pin);
	this.gpioPin = gpio.provisionDigitalMultipurposePin(provider, pin, PinMode.DIGITAL_OUTPUT);
	this.name = name;
	this.low = new Runnable() {
	    public void run() {
		gpioPin.low();
	    }
	};
	this.high = new Runnable() {
	    public void run() {
		gpioPin.high();
	    }
	};
	this.toggle = new Runnable() {
	    public void run() {
		gpioPin.toggle();
	    }
	};
    }

    @Override
    public boolean toggleSwitch() {
	return runOperation(toggle, "toggle");
    }

    @Override
    public boolean switchOn() {
	return runOperation(high, "switch on");
    }

    @Override
    public boolean switchOff() {
	return runOperation(low, "switch off");
    }

    @Override
    public synchronized boolean isSwitchedOn() {
	try {
	    gpioPin.setMode(PinMode.DIGITAL_INPUT);
	    return gpioPin.isHigh();
	} finally {
	    gpioPin.setMode(PinMode.DIGITAL_OUTPUT);
	}
    }

    private synchronized boolean runOperation(Runnable runnable, String operationName) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("GpioControl '" + name + "' operation: " + operationName);
	}
	if (gpioPin != null) {
	    if (isEnabled()) {
		runnable.run();
		return true;
	    } else {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("GpioControl '" + name + "' disabled. Not perfoming action " + operationName);
		}
	    }
	} else {
	    LOG.warn("GpioControl '"
		    + name
		    + "': not attempting to perform any action on the GPIO output since the provisioning did not return any real GPIO output to work with.");
	}
	return false;
    }
}

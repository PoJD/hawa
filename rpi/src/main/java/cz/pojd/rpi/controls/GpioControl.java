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
	this.name = name;
	LOG.info("Creating " + this + " connected to pin " + pin);
	this.gpioPin = gpio.provisionDigitalMultipurposePin(provider, pin, PinMode.DIGITAL_OUTPUT);
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
	if (isInitiated()) {
	    try {
		gpioPin.setMode(PinMode.DIGITAL_INPUT);
		return gpioPin.isHigh();
	    } finally {
		gpioPin.setMode(PinMode.DIGITAL_OUTPUT);
	    }
	} else {
	    logWarnNoInit();
	    return false;
	}
    }

    @Override
    public boolean isInitiated() {
	return gpioPin != null;
    }

    private synchronized boolean runOperation(Runnable runnable, String operationName) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug(this + " operation: " + operationName);
	}
	if (isInitiated()) {
	    if (isEnabled()) {
		runnable.run();
		return true;
	    } else {
		if (LOG.isDebugEnabled()) {
		    LOG.debug(this + " disabled. Not perfoming action " + operationName);
		}
	    }
	} else {
	    logWarnNoInit();
	}
	return false;
    }

    private void logWarnNoInit() {
	LOG.warn(this + ": init failed before, not attempting to run any operation with this control.");
    }

    @Override
    public String toString() {
	return "GpioControl [name=" + name + "]";
    }
}

package cz.pojd.rpi.controls;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

import cz.pojd.rpi.sensors.gpio.Gpio;

/**
 * Control implementation using raw GPIO access (via P4J)
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:56:10 AM
 */
public class GpioControl extends BaseControl implements Control {

    private static final Log LOG = LogFactory.getLog(GpioControl.class);

    private final GpioPinDigitalOutput gpioOutputPin;
    private final String name;
    private final Runnable low, high, toggle;

    @Inject
    public GpioControl(Gpio gpio, String name, Pin pin) {
	LOG.info("Creating GpioControl '" + name + "' connected to pin " + pin);
	this.gpioOutputPin = gpio.provisionDigitalOutputPin(pin);
	this.name = name;
	this.low = new Runnable() {
	    public void run() {
		gpioOutputPin.low();
	    }
	};
	this.high = new Runnable() {
	    public void run() {
		gpioOutputPin.high();
	    }
	};
	this.toggle = new Runnable() {
	    public void run() {
		gpioOutputPin.toggle();
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

    private synchronized boolean runOperation(Runnable runnable, String operationName) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("GpioControl '" + name + "' operation: " + operationName);
	}
	if (gpioOutputPin != null) {
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

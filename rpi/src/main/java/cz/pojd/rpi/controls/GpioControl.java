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
public class GpioControl implements Control {

    private static final Log LOG = LogFactory.getLog(GpioControl.class);

    private final GpioPinDigitalOutput gpioOutputPin;
    private final String name;

    @Inject
    public GpioControl(Gpio gpio, String name, Pin pin) {
	LOG.info("Creating GpioControl '" + name + "' connected to pin " + pin);
	this.gpioOutputPin = gpio.provisionDigitalOutputPin(pin);
	this.name = name;
    }

    @Override
    public void toggleSwitch() {
	logDebug("toogle");
	if (gpioOutputPin != null) {
	    gpioOutputPin.toggle();
	} else {
	    logWarn();
	}
    }

    @Override
    public void switchOn() {
	logDebug("switch on");
	if (gpioOutputPin != null) {
	    gpioOutputPin.high();
	} else {
	    logWarn();
	}
    }

    @Override
    public void switchOff() {
	logDebug("switch off");
	if (gpioOutputPin != null) {
	    gpioOutputPin.low();
	} else {
	    logWarn();
	}
    }

    private void logDebug(String operation) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("GpioControl '" + name + "' operation: " + operation);
	}
    }

    private void logWarn() {
	LOG.warn("GpioControl '" + name + "': not attempting to perform any action on the GPIO output since the provisioning did not return any real GPIO output to work with.");
    }
}

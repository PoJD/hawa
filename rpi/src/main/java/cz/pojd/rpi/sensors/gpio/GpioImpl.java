package cz.pojd.rpi.sensors.gpio;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalog;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

/**
 * Real Gpio implementation using underlying GpioController and delegating all calls to it
 * 
 * @author Lubos Housa
 * @since Aug 4, 2014 9:11:38 PM
 */
public class GpioImpl implements Gpio, DisposableBean {

    private static final Log LOG = LogFactory.getLog(GpioImpl.class);

    private GpioController gpioController;

    @Inject
    public GpioImpl(GpioController gpioController) {
	this.gpioController = gpioController;
    }

    public void export(PinMode mode, GpioPin... pin) {
	gpioController.export(mode, pin);
    }

    public boolean isExported(GpioPin... pin) {
	return gpioController.isExported(pin);
    }

    public void unexport(GpioPin... pin) {
	gpioController.unexport(pin);
    }

    public void unexportAll() {
	gpioController.unexportAll();
    }

    public void setMode(PinMode mode, GpioPin... pin) {
	gpioController.setMode(mode, pin);
    }

    public PinMode getMode(GpioPin pin) {
	return gpioController.getMode(pin);
    }

    public boolean isMode(PinMode mode, GpioPin... pin) {
	return gpioController.isMode(mode, pin);
    }

    public void setPullResistance(PinPullResistance resistance, GpioPin... pin) {
	gpioController.setPullResistance(resistance, pin);
    }

    public PinPullResistance getPullResistance(GpioPin pin) {
	return gpioController.getPullResistance(pin);
    }

    public boolean isPullResistance(PinPullResistance resistance, GpioPin... pin) {
	return gpioController.isPullResistance(resistance, pin);
    }

    public void high(GpioPinDigitalOutput... pin) {
	gpioController.high(pin);
    }

    public boolean isHigh(GpioPinDigital... pin) {
	return gpioController.isHigh(pin);
    }

    public void low(GpioPinDigitalOutput... pin) {
	gpioController.low(pin);
    }

    public boolean isLow(GpioPinDigital... pin) {
	return gpioController.isLow(pin);
    }

    public void setState(PinState state, GpioPinDigitalOutput... pin) {
	gpioController.setState(state, pin);
    }

    public void setState(boolean state, GpioPinDigitalOutput... pin) {
	gpioController.setState(state, pin);
    }

    public boolean isState(PinState state, GpioPinDigital... pin) {
	return gpioController.isState(state, pin);
    }

    public PinState getState(GpioPinDigital pin) {
	return gpioController.getState(pin);
    }

    public void toggle(GpioPinDigitalOutput... pin) {
	gpioController.toggle(pin);
    }

    public void pulse(long milliseconds, GpioPinDigitalOutput... pin) {
	gpioController.pulse(milliseconds, pin);
    }

    public void setValue(double value, GpioPinAnalogOutput... pin) {
	gpioController.setValue(value, pin);
    }

    public double getValue(GpioPinAnalog pin) {
	return gpioController.getValue(pin);
    }

    public void addListener(GpioPinListener listener, GpioPinInput... pin) {
	gpioController.addListener(listener, pin);
    }

    public void addListener(GpioPinListener[] listeners, GpioPinInput... pin) {
	gpioController.addListener(listeners, pin);
    }

    public void removeListener(GpioPinListener listener, GpioPinInput... pin) {
	gpioController.removeListener(listener, pin);
    }

    public void removeListener(GpioPinListener[] listeners, GpioPinInput... pin) {
	gpioController.removeListener(listeners, pin);
    }

    public void removeAllListeners() {
	gpioController.removeAllListeners();
    }

    public void addTrigger(GpioTrigger trigger, GpioPinInput... pin) {
	gpioController.addTrigger(trigger, pin);
    }

    public void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin) {
	gpioController.addTrigger(triggers, pin);
    }

    public void removeTrigger(GpioTrigger trigger, GpioPinInput... pin) {
	gpioController.removeTrigger(trigger, pin);
    }

    public void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin) {
	gpioController.removeTrigger(triggers, pin);
    }

    public void removeAllTriggers() {
	gpioController.removeAllTriggers();
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode,
	    PinPullResistance resistance) {
	return gpioController.provisionDigitalMultipurposePin(provider, pin, name, mode, resistance);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode, PinPullResistance resistance) {
	return gpioController.provisionDigitalMultipurposePin(provider, pin, mode, resistance);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode) {
	return gpioController.provisionDigitalMultipurposePin(provider, pin, name, mode);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode) {
	return gpioController.provisionDigitalMultipurposePin(provider, pin, mode);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode, PinPullResistance resistance) {
	return gpioController.provisionDigitalMultipurposePin(pin, name, mode, resistance);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode, PinPullResistance resistance) {
	return gpioController.provisionDigitalMultipurposePin(pin, mode, resistance);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode) {
	return gpioController.provisionDigitalMultipurposePin(pin, name, mode);
    }

    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode) {
	return gpioController.provisionDigitalMultipurposePin(pin, mode);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name, PinPullResistance resistance) {
	return gpioController.provisionDigitalInputPin(provider, pin, name, resistance);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, PinPullResistance resistance) {
	return gpioController.provisionDigitalInputPin(provider, pin, resistance);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name) {
	return gpioController.provisionDigitalInputPin(provider, pin, name);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin) {
	return gpioController.provisionDigitalInputPin(provider, pin);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance) {
	return gpioController.provisionDigitalInputPin(pin, name, resistance);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, PinPullResistance resistance) {
	return gpioController.provisionDigitalInputPin(pin, resistance);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name) {
	return gpioController.provisionDigitalInputPin(pin, name);
    }

    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin) {
	return gpioController.provisionDigitalInputPin(pin);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name, PinState defaultState) {
	return gpioController.provisionDigitalOutputPin(provider, pin, name, defaultState);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, PinState defaultState) {
	return gpioController.provisionDigitalOutputPin(provider, pin, defaultState);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name) {
	return gpioController.provisionDigitalOutputPin(provider, pin, name);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin) {
	return gpioController.provisionDigitalOutputPin(provider, pin);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name, PinState defaultState) {
	return gpioController.provisionDigitalOutputPin(pin, name, defaultState);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, PinState defaultState) {
	return gpioController.provisionDigitalOutputPin(pin, defaultState);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name) {
	return gpioController.provisionDigitalOutputPin(pin, name);
    }

    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin) {
	return gpioController.provisionDigitalOutputPin(pin);
    }

    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name) {
	return gpioController.provisionAnalogInputPin(provider, pin, name);
    }

    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin) {
	return gpioController.provisionAnalogInputPin(provider, pin);
    }

    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name) {
	return gpioController.provisionAnalogInputPin(pin, name);
    }

    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin) {
	return gpioController.provisionAnalogInputPin(pin);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name, double defaultValue) {
	return gpioController.provisionAnalogOutputPin(provider, pin, name, defaultValue);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, double defaultValue) {
	return gpioController.provisionAnalogOutputPin(provider, pin, defaultValue);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name) {
	return gpioController.provisionAnalogOutputPin(provider, pin, name);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin) {
	return gpioController.provisionAnalogOutputPin(provider, pin);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name, double defaultValue) {
	return gpioController.provisionAnalogOutputPin(pin, name, defaultValue);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, double defaultValue) {
	return gpioController.provisionAnalogOutputPin(pin, defaultValue);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name) {
	return gpioController.provisionAnalogOutputPin(pin, name);
    }

    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin) {
	return gpioController.provisionAnalogOutputPin(pin);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue) {
	return gpioController.provisionPwmOutputPin(provider, pin, name, defaultValue);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue) {
	return gpioController.provisionPwmOutputPin(provider, pin, defaultValue);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name) {
	return gpioController.provisionPwmOutputPin(provider, pin, name);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin) {
	return gpioController.provisionPwmOutputPin(provider, pin);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue) {
	return gpioController.provisionPwmOutputPin(pin, name, defaultValue);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, int defaultValue) {
	return gpioController.provisionPwmOutputPin(pin, defaultValue);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name) {
	return gpioController.provisionPwmOutputPin(pin, name);
    }

    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin) {
	return gpioController.provisionPwmOutputPin(pin);
    }

    public GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode) {
	return gpioController.provisionPin(provider, pin, name, mode);
    }

    public GpioPin provisionPin(GpioProvider provider, Pin pin, PinMode mode) {
	return gpioController.provisionPin(provider, pin, mode);
    }

    public GpioPin provisionPin(Pin pin, String name, PinMode mode) {
	return gpioController.provisionPin(pin, name, mode);
    }

    public GpioPin provisionPin(Pin pin, PinMode mode) {
	return gpioController.provisionPin(pin, mode);
    }

    public void setShutdownOptions(GpioPinShutdown options, GpioPin... pin) {
	gpioController.setShutdownOptions(options, pin);
    }

    public void setShutdownOptions(Boolean unexport, GpioPin... pin) {
	gpioController.setShutdownOptions(unexport, pin);
    }

    public void setShutdownOptions(Boolean unexport, PinState state, GpioPin... pin) {
	gpioController.setShutdownOptions(unexport, state, pin);
    }

    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, GpioPin... pin) {
	gpioController.setShutdownOptions(unexport, state, resistance, pin);
    }

    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode, GpioPin... pin) {
	gpioController.setShutdownOptions(unexport, state, resistance, mode, pin);
    }

    public Collection<GpioPin> getProvisionedPins() {
	return gpioController.getProvisionedPins();
    }

    public void unprovisionPin(GpioPin... pin) {
	gpioController.unprovisionPin(pin);
    }

    public boolean isShutdown() {
	return gpioController.isShutdown();
    }

    public void shutdown() {
	gpioController.shutdown();
    }

    @Override
    public boolean isReal() {
	return true;
    }

    @Override
    public void destroy() throws Exception {
	LOG.info("Cleaning up in GpioImpl: unexporting and unprovisioning...");
	try {
	    // just unexport all pins and unprovision them - so e.g. after redeploy it will still work (i.e. no shutdown here)
	    gpioController.unexportAll();
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Unprovisioning pins: " + getProvisionedPins());
	    }
	    unprovisionPin(getProvisionedPins().toArray(new GpioPin[] {}));
	} catch (Exception e) {
	    LOG.warn("Unable to cleanup GPIO...", e);
	}
    }

    @Override
    public GpioProvider getDefaultProvider() {
	return GpioFactory.getDefaultProvider();
    }
}

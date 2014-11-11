package cz.pojd.rpi.sensors.observable;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.gpio.Gpio;

/**
 * GpioObservableSensor is an observable sensor (listeners can be added to it to observe changes detected by the sensor) using GPIO via Pi4J. PinState
 * is exported as the value to the observers (high usually means the sensor did detect something - e.g. move)
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:23:44 AM
 */
public class GpioObservableSensor extends BaseObservableSensor {

    private static final Log LOG = LogFactory.getLog(GpioObservableSensor.class);

    private final GpioPinDigitalInput gpioInputPin;
    protected final String name;

    @Inject
    public GpioObservableSensor(Gpio gpio, GpioProvider provider, String name, Pin pin) {
	this(gpio, provider, name, pin, PinPullResistance.OFF);
    }

    /**
     * Create new instance of this observable sensor
     * 
     * @param gpio
     *            gpio reference to use for accessing GPIO
     * @param provider
     *            the provider to use for this sensor
     * @param name
     *            the name of this sensor
     * @param pin
     *            the pin to connct to
     */
    @Inject
    public GpioObservableSensor(Gpio gpio, GpioProvider provider, String name, Pin pin, PinPullResistance pinPullResistance) {
	this.name = name;
	LOG.info("Creating " + this + " connected to pin " + pin + " with PinPullResistance " + pinPullResistance + " and using provider: " + provider);
	this.gpioInputPin = gpio.provisionDigitalInputPin(provider, pin, pinPullResistance);
	setupPin();
    }

    @Override
    public List<Reading> readAll() {
	return Collections.singletonList(read());
    }

    @Override
    public Reading read() {
	if (isInitiated()) {
	    return Reading.newBuilder().type(Type.generic).doubleValue(translate(gpioInputPin.getState()).getValue()).build();
	} else {
	    LOG.warn(this + ": init failed before, returning invalid value in read().");
	    return Reading.invalid(Type.generic);
	}
    }

    private void setupPin() {
	if (isInitiated()) {
	    gpioInputPin.addListener(new GpioPinListenerDigital() {
		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		    if (LOG.isDebugEnabled()) {
			LOG.debug(this + ": detected change on pin " + event.getPin() + ". Value = " + event.getState());
		    }
		    notifyObservers(translate(event.getState()).isHigh());
		}
	    });

	    gpioInputPin.setShutdownOptions(true);
	} else {
	    LOG.warn(this + ": init failed before, not setting up any GPIO listener nor any shutdown options.");
	}
    }

    @Override
    public boolean isInitiated() {
	return gpioInputPin != null;
    }

    /**
     * Translate the low level detected state to the outside world (via the API). Descendant classes are allowed to process the value here. The
     * default implementation here does no translation
     * 
     * @param detected
     *            low level detected pin state value to translate
     * @return translated value
     */
    protected PinState translate(PinState detected) {
	return detected;
    }

    @Override
    public String toString() {
	return "GpioObservableSensor [name=" + name + "]";
    }
}

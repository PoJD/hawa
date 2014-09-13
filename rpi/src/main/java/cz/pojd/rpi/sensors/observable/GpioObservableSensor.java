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
    private final String name;

    @Inject
    public GpioObservableSensor(Gpio gpio, String name, Pin pin) {
	this(gpio, gpio.getDefaultProvider(), name, pin);
    }

    @Inject
    public GpioObservableSensor(Gpio gpio, GpioProvider provider, String name, Pin pin) {
	this.name = name;
	LOG.info("Creating " + this + " connected to pin " + pin);
	this.gpioInputPin = gpio.provisionDigitalInputPin(provider, pin, PinPullResistance.PULL_DOWN);
	setupListener();
    }

    @Override
    public List<Reading> readAll() {
	return Collections.singletonList(read());
    }

    @Override
    public Reading read() {
	if (isInitiated()) {
	    return Reading.newBuilder().type(Type.generic).doubleValue(gpioInputPin.getState().getValue()).build();
	} else {
	    LOG.warn(this + ": init failed before, returning invalid value in read().");
	    return Reading.invalid(Type.generic);
	}
    }

    private void setupListener() {
	if (isInitiated()) {
	    gpioInputPin.addListener(new GpioPinListenerDigital() {
		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		    if (LOG.isDebugEnabled()) {
			LOG.debug(this + ": detected change on pin " + event.getPin() + ". Value = " + event.getState());
		    }
		    notifyObservers(event.getState());
		}
	    });
	} else {
	    LOG.warn(this + ": init failed before, not setting up any GPIO listener.");
	}
    }

    @Override
    public boolean isInitiated() {
	return gpioInputPin != null;
    }

    @Override
    public String toString() {
	return "GpioObservableSensor [name=" + name + "]";
    }
}

package cz.pojd.rpi.sensors.observable;

import javax.inject.Inject;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.sensors.gpio.Gpio;

/**
 * GpioSwitch is a special type of GpioObservableSensor - uses internal pull up resistor and swaps the high and low values to still treat HIGH and LOW
 * as would human understand (HIGH means switch is pressed, LOW released). Typical usage for real buton switches
 *
 * @author Lubos Housa
 * @since Nov 11, 2014 8:38:02 PM
 */
public class GpioSwitch extends GpioObservableSensor {

    @Inject
    public GpioSwitch(Gpio gpio, GpioProvider provider, String name, Pin pin) {
	super(gpio, provider, name, pin, PinPullResistance.PULL_UP);
    }

    protected PinState translate(PinState detected) {
	return PinState.getInverseState(detected);
    }

    @Override
    public String toString() {
	return "GpioSwitch [name=" + name + "]";
    }
}

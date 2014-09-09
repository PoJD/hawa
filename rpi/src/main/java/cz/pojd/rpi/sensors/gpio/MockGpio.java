package cz.pojd.rpi.sensors.gpio;

import java.util.Collection;

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
 * Mock Gpio provided at runtime in cases the real one is not available (e.g. running the app not on RasPI).
 * 
 * @author Lubos Housa
 * @since Aug 4, 2014 8:31:36 PM
 */
public class MockGpio implements Gpio {

    @Override
    public void export(PinMode mode, GpioPin... pin) {

    }

    @Override
    public boolean isExported(GpioPin... pin) {
	return false;
    }

    @Override
    public void unexport(GpioPin... pin) {

    }

    @Override
    public void unexportAll() {

    }

    @Override
    public void setMode(PinMode mode, GpioPin... pin) {

    }

    @Override
    public PinMode getMode(GpioPin pin) {
	return null;
    }

    @Override
    public boolean isMode(PinMode mode, GpioPin... pin) {
	return false;
    }

    @Override
    public void setPullResistance(PinPullResistance resistance, GpioPin... pin) {

    }

    @Override
    public PinPullResistance getPullResistance(GpioPin pin) {
	return null;
    }

    @Override
    public boolean isPullResistance(PinPullResistance resistance, GpioPin... pin) {
	return false;
    }

    @Override
    public void high(GpioPinDigitalOutput... pin) {

    }

    @Override
    public boolean isHigh(GpioPinDigital... pin) {
	return false;
    }

    @Override
    public void low(GpioPinDigitalOutput... pin) {

    }

    @Override
    public boolean isLow(GpioPinDigital... pin) {
	return false;
    }

    @Override
    public void setState(PinState state, GpioPinDigitalOutput... pin) {

    }

    @Override
    public void setState(boolean state, GpioPinDigitalOutput... pin) {

    }

    @Override
    public boolean isState(PinState state, GpioPinDigital... pin) {
	return false;
    }

    @Override
    public PinState getState(GpioPinDigital pin) {
	return null;
    }

    @Override
    public void toggle(GpioPinDigitalOutput... pin) {

    }

    @Override
    public void pulse(long milliseconds, GpioPinDigitalOutput... pin) {

    }

    @Override
    public void setValue(double value, GpioPinAnalogOutput... pin) {

    }

    @Override
    public double getValue(GpioPinAnalog pin) {
	return 0;
    }

    @Override
    public void addListener(GpioPinListener listener, GpioPinInput... pin) {

    }

    @Override
    public void addListener(GpioPinListener[] listeners, GpioPinInput... pin) {

    }

    @Override
    public void removeListener(GpioPinListener listener, GpioPinInput... pin) {

    }

    @Override
    public void removeListener(GpioPinListener[] listeners, GpioPinInput... pin) {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void addTrigger(GpioTrigger trigger, GpioPinInput... pin) {

    }

    @Override
    public void addTrigger(GpioTrigger[] triggers, GpioPinInput... pin) {

    }

    @Override
    public void removeTrigger(GpioTrigger trigger, GpioPinInput... pin) {

    }

    @Override
    public void removeTrigger(GpioTrigger[] triggers, GpioPinInput... pin) {

    }

    @Override
    public void removeAllTriggers() {

    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode,
	    PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, String name, PinMode mode) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(GpioProvider provider, Pin pin, PinMode mode) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, String name, PinMode mode) {
	return null;
    }

    @Override
    public GpioPinDigitalMultipurpose provisionDigitalMultipurposePin(Pin pin, PinMode mode) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(GpioProvider provider, Pin pin) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, PinPullResistance resistance) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinDigitalInput provisionDigitalInputPin(Pin pin) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name, PinState defaultState) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, PinState defaultState) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(GpioProvider provider, Pin pin) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name, PinState defaultState) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, PinState defaultState) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinDigitalOutput provisionDigitalOutputPin(Pin pin) {
	return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(GpioProvider provider, Pin pin) {
	return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinAnalogInput provisionAnalogInputPin(Pin pin) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name, double defaultValue) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, double defaultValue) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(GpioProvider provider, Pin pin) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name, double defaultValue) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, double defaultValue) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinAnalogOutput provisionAnalogOutputPin(Pin pin) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name, int defaultValue) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, int defaultValue) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(GpioProvider provider, Pin pin) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name, int defaultValue) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, int defaultValue) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin, String name) {
	return null;
    }

    @Override
    public GpioPinPwmOutput provisionPwmOutputPin(Pin pin) {
	return null;
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, String name, PinMode mode) {
	return null;
    }

    @Override
    public GpioPin provisionPin(GpioProvider provider, Pin pin, PinMode mode) {
	return null;
    }

    @Override
    public GpioPin provisionPin(Pin pin, String name, PinMode mode) {
	return null;
    }

    @Override
    public GpioPin provisionPin(Pin pin, PinMode mode) {
	return null;
    }

    @Override
    public void setShutdownOptions(GpioPinShutdown options, GpioPin... pin) {

    }

    @Override
    public void setShutdownOptions(Boolean unexport, GpioPin... pin) {

    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, GpioPin... pin) {

    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, GpioPin... pin) {

    }

    @Override
    public void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode, GpioPin... pin) {

    }

    @Override
    public Collection<GpioPin> getProvisionedPins() {
	return null;
    }

    @Override
    public void unprovisionPin(GpioPin... pin) {

    }

    @Override
    public boolean isShutdown() {
	return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isReal() {
	return false;
    }

    @Override
    public GpioProvider getDefaultProvider() {
	return null;
    }
}

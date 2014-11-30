package cz.pojd.homeautomation.model.spring;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

public class OutdoorSpecification {

    private boolean newRaspi;
    private int altitude;
    private int dhtSensorSysClassPin;
    private Pin motionSensorPin;
    private Pin lightSwitchPin, lightControlPin, lightLevelSensorPin;
    private double lightLevelTreshold;
    private GpioProvider gpioProvider;

    public boolean isNewRaspi() {
	return newRaspi;
    }

    public void setNewRaspi(boolean newRaspi) {
	this.newRaspi = newRaspi;
    }

    public int getAltitude() {
	return altitude;
    }

    public void setAltitude(int altitude) {
	this.altitude = altitude;
    }

    public int getDhtSensorSysClassPin() {
	return dhtSensorSysClassPin;
    }

    public void setDhtSensorSysClassPin(int dhtSensorSysClassPin) {
	this.dhtSensorSysClassPin = dhtSensorSysClassPin;
    }

    public Pin getMotionSensorPin() {
	return motionSensorPin;
    }

    public void setMotionSensorPin(Pin motionSensorPin) {
	this.motionSensorPin = motionSensorPin;
    }

    public Pin getLightSwitchPin() {
	return lightSwitchPin;
    }

    public void setLightSwitchPin(Pin lightSwitchPin) {
	this.lightSwitchPin = lightSwitchPin;
    }

    public Pin getLightControlPin() {
	return lightControlPin;
    }

    public void setLightControlPin(Pin lightControlPin) {
	this.lightControlPin = lightControlPin;
    }

    public Pin getLightLevelSensorPin() {
	return lightLevelSensorPin;
    }

    public void setLightLevelSensorPin(Pin lightLevelSensorPin) {
	this.lightLevelSensorPin = lightLevelSensorPin;
    }

    public double getLightLevelTreshold() {
	return lightLevelTreshold;
    }

    public void setLightLevelTreshold(double lightLevelTreshold) {
	this.lightLevelTreshold = lightLevelTreshold;
    }

    public GpioProvider getGpioProvider() {
	return gpioProvider;
    }

    public void setGpioProvider(GpioProvider gpioProvider) {
	this.gpioProvider = gpioProvider;
    }

    @Override
    public String toString() {
	return "OutdoorSpecification [newRaspi=" + newRaspi + ", altitude=" + altitude + ", dhtSensorSysClassPin=" + dhtSensorSysClassPin
		+ ", motionSensorPin=" + motionSensorPin + ", lightSwitchPin=" + lightSwitchPin + ", lightControlPin=" + lightControlPin
		+ ", lightLevelSensorPin=" + lightLevelSensorPin + ", lightLevelTreshold=" + lightLevelTreshold + ", gpioProvider=" + gpioProvider
		+ "]";
    }
}

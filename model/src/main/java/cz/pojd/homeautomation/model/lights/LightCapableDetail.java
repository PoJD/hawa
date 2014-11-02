package cz.pojd.homeautomation.model.lights;

import cz.pojd.rpi.State;
import cz.pojd.rpi.sensors.Reading;

public abstract class LightCapableDetail {
    private State motionSensor, lightSwitch, lightControl;
    private Reading lightLevel;

    protected LightCapableDetail() {
    }

    protected LightCapableDetail(LightCapable lightCapable) {
	resetFrom(lightCapable);
    }

    protected LightCapableDetail(LightCapableDetail detail) {
	resetFrom(detail);
    }

    public void resetFrom(LightCapable lightCapable) {
	setMotionSensor(lightCapable.getMotionSensor() != null ? lightCapable.getMotionSensor().getState() : null);
	setLightSwitch(lightCapable.getLightSwitch() != null ? lightCapable.getLightSwitch().getState() : null);
	setLightControl(lightCapable.getLightControl() != null ? lightCapable.getLightControl().getState() : null);
	setLightLevel(lightCapable.getLightLevelSensor() != null ? lightCapable.getLightLevelSensor().read() : null);
    }

    public void resetFrom(LightCapableDetail detail) {
	setMotionSensor(detail.getMotionSensor());
	setLightSwitch(detail.getLightSwitch());
	setLightControl(detail.getLightControl());
	setLightLevel(detail.getLightLevel());
    }

    public State getMotionSensor() {
	return motionSensor;
    }

    public void setMotionSensor(State motionSensor) {
	this.motionSensor = motionSensor;
    }

    public State getLightSwitch() {
	return lightSwitch;
    }

    public void setLightSwitch(State lightSwitch) {
	this.lightSwitch = lightSwitch;
    }

    public State getLightControl() {
	return lightControl;
    }

    public void setLightControl(State lightControl) {
	this.lightControl = lightControl;
    }

    public Reading getLightLevel() {
	return lightLevel;
    }

    public void setLightLevel(Reading lightLevel) {
	this.lightLevel = lightLevel;
    }
}

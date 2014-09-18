package cz.pojd.homeautomation.hawa.lights;

import cz.pojd.rpi.State;

public abstract class LightCapableDetail {
    private State motionSensor, lightSwitch, lightControl;

    protected LightCapableDetail() {
    }

    protected LightCapableDetail(LightCapable lightCapable) {
	resetFrom(lightCapable);
    }

    protected LightCapableDetail(LightCapableDetail detail) {
	resetFrom(detail);
    }

    protected void resetFrom(LightCapable lightCapable) {
	setMotionSensor(lightCapable.getMotionSensor() != null ? lightCapable.getMotionSensor().getState() : null);
	setLightSwitch(lightCapable.getLightSwitch() != null ? lightCapable.getLightSwitch().getState() : null);
	setLightControl(lightCapable.getLightControl() != null ? lightCapable.getLightControl().getState() : null);
    }

    public void resetFrom(LightCapableDetail detail) {
	setMotionSensor(detail.getMotionSensor());
	setLightSwitch(detail.getLightSwitch());
	setLightControl(detail.getLightControl());
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
}

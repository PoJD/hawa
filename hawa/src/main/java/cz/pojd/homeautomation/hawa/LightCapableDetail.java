package cz.pojd.homeautomation.hawa;

import cz.pojd.rpi.sensors.SensorState;

public abstract class LightCapableDetail {
    private SensorState motionSensor, lightSwitch, lightControl;

    protected LightCapableDetail() {
    }

    protected LightCapableDetail(LightCapable lightCapable) {
	setMotionSensor(lightCapable.getMotionSensor() != null ? lightCapable.getMotionSensor().asSensorState() : null);
	setLightSwitch(lightCapable.getLightSwitch() != null ? lightCapable.getLightSwitch().asSensorState() : null);
	setLightControl(lightCapable.getLightControl() != null ? lightCapable.getLightControl().asSensorState() : null);
    }

    protected LightCapableDetail(LightCapableDetail detail) {
	setFrom(detail);
    }

    public void setFrom(LightCapableDetail detail) {
	setMotionSensor(detail.getMotionSensor());
	setLightSwitch(detail.getLightSwitch());
	setLightControl(detail.getLightControl());
    }

    public SensorState getMotionSensor() {
	return motionSensor;
    }

    public void setMotionSensor(SensorState motionSensor) {
	this.motionSensor = motionSensor;
    }

    public SensorState getLightSwitch() {
	return lightSwitch;
    }

    public void setLightSwitch(SensorState lightSwitch) {
	this.lightSwitch = lightSwitch;
    }

    public SensorState getLightControl() {
	return lightControl;
    }

    public void setLightControl(SensorState lightControl) {
	this.lightControl = lightControl;
    }
}

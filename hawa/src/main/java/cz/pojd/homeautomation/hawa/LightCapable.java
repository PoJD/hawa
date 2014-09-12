package cz.pojd.homeautomation.hawa;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Generic light capable entity. It contains light control, light switch and motion sensor (all optional). All retrieval and setter methods available
 * here
 *
 * @author Lubos Housa
 * @since Sep 11, 2014 8:44:25 PM
 */
public abstract class LightCapable {
    private ObservableSensor motionSensor;
    private ObservableSensor lightSwitch;
    private Control lightControl;

    public ObservableSensor getMotionSensor() {
	return motionSensor;
    }

    public void setMotionSensor(ObservableSensor motionSensor) {
	this.motionSensor = motionSensor;
    }

    public ObservableSensor getLightSwitch() {
	return lightSwitch;
    }

    public void setLightSwitch(ObservableSensor lightSwitch) {
	this.lightSwitch = lightSwitch;
    }

    public Control getLightControl() {
	return lightControl;
    }

    public void setLightControl(Control lightControl) {
	this.lightControl = lightControl;
    }

    public void resetFrom(LightCapableDetail detail) {
	if (getMotionSensor() != null) {
	    getMotionSensor().resetFrom(detail.getMotionSensor());
	}
	if (getLightSwitch() != null) {
	    getLightSwitch().resetFrom(detail.getLightSwitch());
	}
	if (getLightControl() != null) {
	    getLightControl().resetFrom(detail.getLightControl());
	}
    }
}

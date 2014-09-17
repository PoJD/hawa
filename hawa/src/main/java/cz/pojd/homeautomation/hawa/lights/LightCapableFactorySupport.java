package cz.pojd.homeautomation.hawa.lights;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

import cz.pojd.rpi.controllers.ControlDependentObserver;
import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;

public abstract class LightCapableFactorySupport {

    protected void enrichLight(LightCapable lightCapable, Gpio gpio, GpioProvider switchProvider, GpioProvider controlProvider, Pin motionSensorPin,
	    Pin lightSwitchPin, Pin lightControlPin) {
	lightCapable.setLightControl(new GpioControl(gpio, controlProvider, lightCapable.getName() + " light control", lightControlPin));

	lightCapable.setLightSwitch(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " light switch", lightSwitchPin, true));
	lightCapable.getLightSwitch().addObserver(new ControlObserver(lightCapable.getLightControl()));

	if (motionSensorPin != null) {
	    // motion sensor depends on light switch - so that the motion sensor does nothing if light switch is enabled and switched on
	    lightCapable.setMotionSensor(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " motion sensor", motionSensorPin));
	    lightCapable.getMotionSensor().addObserver(new ControlDependentObserver(lightCapable.getLightSwitch(), lightCapable.getLightControl()));
	}
    }
}

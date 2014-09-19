package cz.pojd.homeautomation.hawa.lights;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

import cz.pojd.rpi.controllers.ControlDependentObserver;
import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;

public abstract class LightCapableFactorySupport {

    protected void enrichLight(final LightCapable lightCapable, Gpio gpio, GpioProvider switchProvider, GpioProvider controlProvider,
	    Pin motionSensorPin, Pin lightSwitchPin, Pin lightControlPin) {
	lightCapable.setLightControl(new GpioControl(gpio, controlProvider, lightCapable.getName() + " light control", lightControlPin));

	lightCapable.setLightSwitch(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " light switch", lightSwitchPin, true));
	lightCapable.getLightSwitch().addObserver(new ControlObserver(lightCapable.getLightControl()));

	// #15 this makes sure that after the primary observer does it work (e.g. light switch fired and control changed), the respective details are
	// updated too
	// the same logic then below in motion sensor
	Observer lightCapableUpdatingObserver = new LightCapableUpdatingObserver(lightCapable);
	lightCapable.getLightSwitch().addObserver(lightCapableUpdatingObserver);

	if (motionSensorPin != null) {
	    // motion sensor depends on light switch - so that the motion sensor does nothing if light switch is enabled and switched on
	    lightCapable.setMotionSensor(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " motion sensor", motionSensorPin));
	    lightCapable.getMotionSensor().addObserver(new ControlDependentObserver(lightCapable.getLightSwitch(), lightCapable.getLightControl()));
	    lightCapable.getMotionSensor().addObserver(lightCapableUpdatingObserver);
	}
    }
}

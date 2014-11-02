package cz.pojd.homeautomation.model.lights;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controllers.SensorDisabledOrOffSpecification;
import cz.pojd.rpi.controllers.SpecificationsDependentObserver;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.i2c.TSL2561LightSensor;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;

public abstract class LightCapableFactorySupport {

    protected void enrichLight(final LightCapable lightCapable, Gpio gpio, GpioProvider switchProvider, GpioProvider controlProvider,
	    Pin motionSensorPin, Pin lightSwitchPin, Pin lightControlPin, boolean isNewRaspi, int lightLevelSensorAddress, double lightLevelTreshold) {
	lightCapable.setLightControl(new GpioControl(gpio, controlProvider, lightCapable.getName() + " light control", lightControlPin));

	lightCapable.setLightSwitch(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " light switch", lightSwitchPin));
	lightCapable.getLightSwitch().addObserver(new ControlObserver(lightCapable.getLightControl()));

	// #15 this makes sure that after the primary observer does it work (e.g. light switch fired and control changed), the respective details are
	// updated too
	// the same logic then below in motion sensor
	Observer lightCapableUpdatingObserver = new LightCapableUpdatingObserver(lightCapable);
	lightCapable.getLightSwitch().addObserver(lightCapableUpdatingObserver);

	if (motionSensorPin != null) {
	    // light level sensor only present if we have motion sensor
	    lightCapable.setLightLevelSensor(new TSL2561LightSensor(isNewRaspi, lightLevelSensorAddress));

	    // motion sensor depends on light switch - so that the motion sensor does nothing if light switch is enabled and switched on
	    // it also depends on light level sensor - if the sensor reads too high (too bright), then the motion sensor does nothing too
	    lightCapable.setMotionSensor(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " motion sensor", motionSensorPin));
	    lightCapable.getMotionSensor().addObserver(
		    new SpecificationsDependentObserver(lightCapable.getLightControl(), new SensorDisabledOrOffSpecification(lightCapable
			    .getLightSwitch()), new LightLevelBelowTresholdOrControlOnSpecification(lightCapable, lightLevelTreshold, lightCapable
			    .getLightControl())));
	    lightCapable.getMotionSensor().addObserver(lightCapableUpdatingObserver);
	}
    }
}

package cz.pojd.homeautomation.model.lights;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.controllers.SensorDisabledOrOffSpecification;
import cz.pojd.rpi.controllers.SpecificationsDependentObserver;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.i2c.TSL2561LightSensor;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;

/**
 * Trigger for lights - sets up the observers on the motion sensors on the lights
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 12:41:56 AM
 */
public class DefaultMotionSensorLightTrigger implements MotionSensorLightTrigger {
    private static final Log LOG = LogFactory.getLog(DefaultMotionSensorLightTrigger.class);

    public final Gpio gpio;
    public final boolean isNewRaspi;

    public DefaultMotionSensorLightTrigger(Gpio gpio, boolean isNewRaspi) {
	this.gpio = gpio;
	this.isNewRaspi = isNewRaspi;
    }

    @Override
    public void setup(MotionSensorLightDetails details) {
	LightCapable lightCapable = details.getLightCapable();

	LOG.info("Hooking up lights for " + lightCapable.getName() + " ...");

	lightCapable.setLightControl(new GpioControl(gpio, details.getControlProvider(), lightCapable.getName() + " light control", details
		.getLightControlPin()));

	lightCapable.setLightSwitch(new GpioObservableSensor(gpio, details.getSwitchProvider(), lightCapable.getName() + " light switch", details
		.getLightSwitchPin()));
	lightCapable.getLightSwitch().addObserver(new ControlObserver(lightCapable.getLightControl()));

	// #15 this makes sure that after the primary observer does it work (e.g. light switch fired and control changed), the respective details are
	// updated too
	// the same logic then below in motion sensor
	Observer lightCapableUpdatingObserver = new LightCapableUpdatingObserver(lightCapable);
	lightCapable.getLightSwitch().addObserver(lightCapableUpdatingObserver);

	if (details.getMotionSensorPin() != null) {
	    // light level sensor only present if we have motion sensor
	    lightCapable.setLightLevelSensor(new TSL2561LightSensor(isNewRaspi, details.getLightLevelSensorAddress()));

	    // motion sensor depends on light switch - so that the motion sensor does nothing if light switch is enabled and switched on
	    // it also depends on light level sensor - if the sensor reads too high (too bright), then the motion sensor does nothing too
	    lightCapable.setMotionSensor(new GpioObservableSensor(gpio, details.getSwitchProvider(), lightCapable.getName() + " motion sensor",
		    details.getMotionSensorPin()));
	    lightCapable.getMotionSensor().addObserver(
		    new SpecificationsDependentObserver(lightCapable.getLightControl(), new SensorDisabledOrOffSpecification(lightCapable
			    .getLightSwitch()), new LightLevelBelowTresholdOrControlOnSpecification(lightCapable, details.getLightLevelTreshold(),
			    lightCapable.getLightControl())));
	    lightCapable.getMotionSensor().addObserver(lightCapableUpdatingObserver);
	}
	LOG.info("Done");
    }
}
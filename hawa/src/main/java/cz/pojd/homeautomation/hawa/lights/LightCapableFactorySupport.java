package cz.pojd.homeautomation.hawa.lights;

import java.util.Observable;
import java.util.Observer;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.controllers.ControlDependentObserver;
import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;

public abstract class LightCapableFactorySupport {

    protected void enrichLight(final LightCapable lightCapable, Gpio gpio, GpioProvider switchProvider, GpioProvider controlProvider,
	    Pin motionSensorPin, Pin lightSwitchPin, Pin lightControlPin) {
	lightCapable.setLightControl(new GpioControl(gpio, controlProvider, lightCapable.getName() + " light control", lightControlPin));

	lightCapable.setLightSwitch(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " light switch", lightSwitchPin, true));
	lightCapable.getLightSwitch().addObserver(new ControlObserver(lightCapable.getLightControl()));
	lightCapable.getLightSwitch().addObserver(new Observer() {
	    public void update(Observable o, Object arg) {
		if (lightCapable.getLastDetail() != null) {
		    lightCapable.getLastDetail().getLightSwitch().setSwitchedOn(((PinState) arg).isHigh());
		}
	    }
	});

	if (motionSensorPin != null) {
	    // motion sensor depends on light switch - so that the motion sensor does nothing if light switch is enabled and switched on
	    lightCapable.setMotionSensor(new GpioObservableSensor(gpio, switchProvider, lightCapable.getName() + " motion sensor", motionSensorPin));
	    lightCapable.getMotionSensor().addObserver(new ControlDependentObserver(lightCapable.getLightSwitch(), lightCapable.getLightControl()));
	    lightCapable.getMotionSensor().addObserver(new Observer() {
		public void update(Observable o, Object arg) {
		    if (lightCapable.getLastDetail() != null) {
			lightCapable.getLastDetail().getMotionSensor().setSwitchedOn(((PinState) arg).isHigh());
		    }
		}
	    });
	}
    }
}

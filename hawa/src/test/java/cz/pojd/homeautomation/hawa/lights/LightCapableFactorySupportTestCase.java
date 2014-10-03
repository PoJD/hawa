package cz.pojd.homeautomation.hawa.lights;

import java.io.IOException;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import cz.pojd.rpi.MockObservableSensor;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

public class LightCapableFactorySupportTestCase {

    private LightCapableFactorySupport support;

    @Mocked
    private LightCapable lightCapable;
    @Mocked
    private LightCapableDetail detail;
    @Mocked
    private Reading reading;
    @Mocked
    private Gpio gpio;
    @Mocked
    private GpioProvider provider;
    @Mocked
    private Pin pin;
    @Mocked
    private ObservableSensor mockSensor;
    @Mocked
    private Control mockControl;
    
    @Mocked
    private I2CFactory i2cFactory;
    @Mocked
    private I2CBus i2cBus;
    @Mocked
    private I2CDevice device;
    
    @Before
    public void setup() throws IOException {
	support = new LightCapableFactorySupport() {
	};
	new NonStrictExpectations() {
	    {
		I2CFactory.getInstance(withEqual(I2CBus.BUS_1));
		result = i2cBus;

		i2cBus.getDevice(withEqual(0));
		result = device;
	    }
	};
    }

    @Test
    public void testEnrichLightSetsLightSwitchAndLightControlAlwaysMotionSensorNotIfNotPassed() {
	new NonStrictExpectations() {
	    {
		lightCapable.setLightSwitch(withInstanceOf(ObservableSensor.class));
		times = 1;

		lightCapable.setLightControl(withInstanceOf(Control.class));
		times = 1;

		lightCapable.setMotionSensor(withInstanceOf(ObservableSensor.class));
		maxTimes = 0;

		lightCapable.getLightSwitch();
		result = mockSensor;
	    }
	};

	support.enrichLight(lightCapable, gpio, provider, provider, null, pin, pin, true, 0, 0);
    }

    @Test
    public void testEnrichLightSetsMotionSensorTooIfPassed() {
	new NonStrictExpectations() {
	    {
		lightCapable.setLightSwitch(withInstanceOf(ObservableSensor.class));
		times = 1;

		lightCapable.setLightControl(withInstanceOf(Control.class));
		times = 1;

		lightCapable.setMotionSensor(withInstanceOf(ObservableSensor.class));
		times = 1;

		lightCapable.getLightSwitch();
		result = mockSensor;

		lightCapable.getMotionSensor();
		result = mockSensor;
	    }
	};

	support.enrichLight(lightCapable, gpio, provider, provider, pin, pin, pin, true, 0, 0);
    }

    @Test
    public void testLightSwitchFireHighSwitchesOnControlAndChangesDetails() {
	final MockObservableSensor lightSwitchsensor = new MockObservableSensor();

	new NonStrictExpectations() {
	    {
		lightCapable.getLightSwitch();
		result = lightSwitchsensor;

		lightCapable.getLightControl();
		result = mockControl;

		lightCapable.getLastDetail();
		result = detail;

		mockControl.setSwitchedOn(withEqual(Boolean.TRUE));
		times = 1;

		detail.resetFrom(withEqual(lightCapable));
		times = 1;
	    }
	};

	support.enrichLight(lightCapable, gpio, provider, provider, null, pin, pin, true, 0, 0);
	lightSwitchsensor.notifyObservers(true);
    }

    @Test
    public void testMotionSensorFireLowLightBelowTresholdSwitchesOffControlAndChangesDetails() {
	final MockObservableSensor motionSensor = new MockObservableSensor();

	new NonStrictExpectations() {
	    {
		lightCapable.getLightSwitch();
		result = mockSensor;

		lightCapable.getMotionSensor();
		result = motionSensor;

		lightCapable.getLightControl();
		result = mockControl;

		lightCapable.getLastDetail();
		result = detail;

		detail.getLightLevel();
		result = reading;

		reading.getDoubleValue();
		result = 55;

		mockControl.setSwitchedOn(withEqual(Boolean.FALSE));
		times = 1;

		detail.resetFrom(withEqual(lightCapable));
		times = 1;
	    }
	};

	support.enrichLight(lightCapable, gpio, provider, provider, pin, pin, pin, true, 0, 60);
	motionSensor.notifyObservers(false);
    }

    @Test
    public void testMotionSensorFireLowLightAboveTresholdDoesNotSwitchAnythingButChangesDetails() {
	final MockObservableSensor motionSensor = new MockObservableSensor();

	new NonStrictExpectations() {
	    {
		lightCapable.getLightSwitch();
		result = mockSensor;

		lightCapable.getMotionSensor();
		result = motionSensor;

		lightCapable.getLightControl();
		result = mockControl;

		lightCapable.getLastDetail();
		result = detail;

		detail.getLightLevel();
		result = reading;

		reading.getDoubleValue();
		result = 65;

		mockControl.setSwitchedOn(anyBoolean);
		maxTimes = 0;

		detail.resetFrom(withEqual(lightCapable));
		times = 1;
	    }
	};

	support.enrichLight(lightCapable, gpio, provider, provider, pin, pin, pin, true, 0, 60);
	motionSensor.notifyObservers(false);
    }

    @Test
    public void testMotionSensorFireLowLighSwitchIsOnDoesNotChangeControlButChangesDetails() {
	final MockObservableSensor motionSensor = new MockObservableSensor();

	new NonStrictExpectations() {
	    {
		lightCapable.getLightSwitch();
		result = mockSensor;

		mockSensor.isSwitchedOn();
		result = true;

		mockSensor.isEnabled();
		result = true;

		lightCapable.getMotionSensor();
		result = motionSensor;

		lightCapable.getLightControl();
		result = mockControl;

		lightCapable.getLastDetail();
		result = detail;

		mockControl.setSwitchedOn(anyBoolean);
		maxTimes = 0;

		detail.resetFrom(withEqual(lightCapable));
		times = 1;
	    }
	};

	support.enrichLight(lightCapable, gpio, provider, provider, pin, pin, pin, true, 0, 0);
	motionSensor.notifyObservers(false);
    }
}

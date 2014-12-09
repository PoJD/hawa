package cz.pojd.homeautomation.model.lights;

import java.io.IOException;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.gpio.extension.mcp.MCP3008Pin;
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

public class DefaultMotionSensorLightTriggerTestCase {

    private DefaultMotionSensorLightTrigger lightTrigger;

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
	lightTrigger = new DefaultMotionSensorLightTrigger(gpio);
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

	lightTrigger.setup(MotionSensorLightDetails.newBuilder().lightCapable(lightCapable).switchProvider(provider).controlProvider(provider)
		.lightControlPin(pin).lightSwitchPin(pin).build());
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

	lightTrigger.setup(MotionSensorLightDetails.newBuilder().lightCapable(lightCapable).switchProvider(provider).controlProvider(provider)
		.lightControlPin(pin).lightSwitchPin(pin).motionSensorPin(pin).lightLevelTreshold(0.).lightLevelSensorPin(MCP3008Pin.CH0)
		.build());
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
	lightTrigger.setup(MotionSensorLightDetails.newBuilder().lightCapable(lightCapable).switchProvider(provider).controlProvider(provider)
		.lightControlPin(pin).lightSwitchPin(pin).build());

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

	lightTrigger.setup(MotionSensorLightDetails.newBuilder().lightCapable(lightCapable).switchProvider(provider).controlProvider(provider)
		.lightControlPin(pin).lightSwitchPin(pin).motionSensorPin(pin).lightLevelTreshold(60.).lightLevelSensorPin(MCP3008Pin.CH0)
		.build());
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

	lightTrigger.setup(MotionSensorLightDetails.newBuilder().lightCapable(lightCapable).switchProvider(provider).controlProvider(provider)
		.lightControlPin(pin).lightSwitchPin(pin).motionSensorPin(pin).lightLevelTreshold(60.).lightLevelSensorPin(MCP3008Pin.CH0)
		.build());
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

	lightTrigger.setup(MotionSensorLightDetails.newBuilder().lightCapable(lightCapable).switchProvider(provider).controlProvider(provider)
		.lightControlPin(pin).lightSwitchPin(pin).motionSensorPin(pin).lightLevelTreshold(0.).lightLevelSensorPin(MCP3008Pin.CH0)
		.build());
	motionSensor.notifyObservers(false);
    }
}

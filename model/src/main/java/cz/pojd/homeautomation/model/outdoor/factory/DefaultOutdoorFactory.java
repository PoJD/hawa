package cz.pojd.homeautomation.model.outdoor.factory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.model.lights.MotionSensorLightDetails;
import cz.pojd.homeautomation.model.lights.MotionSensorLightTrigger;
import cz.pojd.homeautomation.model.outdoor.Outdoor;
import cz.pojd.homeautomation.model.outdoor.OutdoorDetail;
import cz.pojd.homeautomation.model.spring.OutdoorSpecification;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.gpio.Dht22Am2302TemperatureAndHumiditySensor;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.i2c.Bmp180BarometricSensor;

public class DefaultOutdoorFactory implements OutdoorFactory {

    private static final Log LOG = LogFactory.getLog(DefaultOutdoorFactory.class);

    @Inject
    private Gpio gpio;
    @Inject
    private MotionSensorLightTrigger lightTrigger;

    public Gpio getGpio() {
	return gpio;
    }

    public void setGpio(Gpio gpio) {
	this.gpio = gpio;
    }

    public MotionSensorLightTrigger getLightTrigger() {
	return lightTrigger;
    }

    public void setLightTrigger(MotionSensorLightTrigger lightTrigger) {
	this.lightTrigger = lightTrigger;
    }

    @Override
    public Outdoor create(OutdoorSpecification outdoorSpecification) {
	LOG.info("Creating new instance of outdoor using: " + outdoorSpecification);
	Outdoor outdoor = new Outdoor();

	List<Sensor> sensors = new ArrayList<>();
	sensors.add(new Bmp180BarometricSensor(outdoorSpecification.isNewRaspi(), outdoorSpecification.getAltitude()));
	sensors.add(new Dht22Am2302TemperatureAndHumiditySensor(outdoorSpecification.getDhtSensorSysClassPin()));
	outdoor.setSensors(sensors);

	lightTrigger.setup(MotionSensorLightDetails.newBuilder()
		.lightCapable(outdoor)
		.switchProvider(outdoorSpecification.getGpioProvider())
		.controlProvider(outdoorSpecification.getGpioProvider())
		.motionSensorPin(outdoorSpecification.getMotionSensorPin())
		.lightSwitchPin(outdoorSpecification.getLightSwitchPin())
		.lightControlPin(outdoorSpecification.getLightControlPin())
		.lightLevelSensorChannel(outdoorSpecification.getLightLevelSensorChannel())
		.lightLevelTreshold(outdoorSpecification.getLightLevelTreshold())
		.build());

	outdoor.setLastDetail(new OutdoorDetail(outdoor));

	LOG.info("New outdoor created: " + outdoor);
	return outdoor;
    }
}

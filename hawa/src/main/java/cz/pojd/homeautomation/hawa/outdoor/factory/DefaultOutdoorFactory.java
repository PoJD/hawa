package cz.pojd.homeautomation.hawa.outdoor.factory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.homeautomation.hawa.lights.LightCapableFactorySupport;
import cz.pojd.homeautomation.hawa.outdoor.Outdoor;
import cz.pojd.homeautomation.hawa.outdoor.OutdoorDetail;
import cz.pojd.homeautomation.hawa.spring.OutdoorSpecification;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.gpio.Dht22Am2302TemperatureAndHumiditySensor;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.i2c.Bmp180BarometricSensor;

public class DefaultOutdoorFactory extends LightCapableFactorySupport implements OutdoorFactory {

    private static final Log LOG = LogFactory.getLog(DefaultOutdoorFactory.class);

    @Inject
    private Gpio gpio;

    public Gpio getGpio() {
	return gpio;
    }

    public void setGpio(Gpio gpio) {
	this.gpio = gpio;
    }

    @Override
    public Outdoor create(OutdoorSpecification outdoorSpecification) {
	LOG.info("Creating new instance of outdoor using: " + outdoorSpecification);
	Outdoor outdoor = new Outdoor();

	List<Sensor> sensors = new ArrayList<>();
	sensors.add(new Bmp180BarometricSensor(outdoorSpecification.isNewRaspi(), outdoorSpecification.getAltitude()));
	sensors.add(new Dht22Am2302TemperatureAndHumiditySensor(outdoorSpecification.getDhtSensorSysClassPin()));
	outdoor.setSensors(sensors);

	enrichLight(outdoor, getGpio(), outdoorSpecification.getGpioProvider(), outdoorSpecification.getGpioProvider(),
		outdoorSpecification.getMotionSensorPin(), outdoorSpecification.getLightSwitchPin(), outdoorSpecification.getLightControlPin(),
		outdoorSpecification.isNewRaspi(), outdoorSpecification.getLightLevelSensorAddress(), outdoorSpecification.getLightLevelTreshold());

	outdoor.setLastDetail(new OutdoorDetail(outdoor));

	LOG.info("New outdoor created: " + outdoor);
	return outdoor;
    }
}

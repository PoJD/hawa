package cz.pojd.homeautomation.hawa.outdoor.factory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cz.pojd.homeautomation.hawa.outdoor.Outdoor;
import cz.pojd.homeautomation.hawa.spring.OutdoorSpecification;
import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.gpio.Dht22Am2302TemperatureAndHumiditySensor;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.i2c.Bmp180BarometricSensor;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;

public class DefaultOutdoorFactory implements OutdoorFactory {

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
	Outdoor outdoor = new Outdoor();

	List<Sensor> sensors = new ArrayList<>();
	sensors.add(new Bmp180BarometricSensor(outdoorSpecification.isNewRaspi(), outdoorSpecification.getAltitude()));
	sensors.add(new Dht22Am2302TemperatureAndHumiditySensor(outdoorSpecification.getDhtSensorSysClassPin()));
	outdoor.setSensors(sensors);

	outdoor.setLightControl(new GpioControl(getGpio(), outdoorSpecification.getGpioProvider(), "Outdoor light control", outdoorSpecification
		.getLightControlPin()));
	outdoor.setLightSwitch(new GpioObservableSensor(getGpio(), outdoorSpecification.getGpioProvider(), "Outdoor light switch",
		outdoorSpecification.getLightSwitchPin()));
	outdoor.setMotionSensor(new GpioObservableSensor(getGpio(), outdoorSpecification.getGpioProvider(), "Outdoor motion sensor",
		outdoorSpecification.getMotionSensorPin()));

	// now setup the wiring between these
	outdoor.getLightSwitch().addObserver(new ControlObserver(outdoor.getLightControl()));
	outdoor.getMotionSensor().addObserver(new ControlObserver(outdoor.getLightControl()));

	return outdoor;
    }
}

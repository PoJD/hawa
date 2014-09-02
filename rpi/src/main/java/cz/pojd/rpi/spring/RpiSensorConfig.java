package cz.pojd.rpi.spring;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.rpi.controllers.MotionSensorController;
import cz.pojd.rpi.controls.CameraControl;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.controls.MjpegStreamerCameraControl;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;
import cz.pojd.rpi.sensors.gpio.Dht22Am2302TemperatureAndHumiditySensor;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.gpio.GpioImpl;
import cz.pojd.rpi.sensors.gpio.MockGpio;
import cz.pojd.rpi.sensors.i2c.Bmp180BarometricSensor;
import cz.pojd.rpi.sensors.observable.GpioObservableSensor;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Spring configuration holding all sensor settings
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
public class RpiSensorConfig {
    private static final Log LOG = LogFactory.getLog(RpiSensorConfig.class);

    @Inject
    private RpiConfig rpiConfig;

    /**
     * Detects the current altitude to adjust the barometric pressure readings or other values dependent on
     * 
     * @return current altitude where the sensors attached to RasPI are located
     */
    @Bean
    public int altitude() {
	return 290;
    }

    @Bean
    public Sensor barometricSensor() {
	return new Bmp180BarometricSensor(rpiConfig.newRasPI(), altitude());
    }

    @Bean
    public Sensor temperatureAndHumiditySensor() {
	// GPIO pin that the DHT22/AM2302 sensor is connected to (following /sys/class naming)
	// GPIO17 is P0
	return new Dht22Am2302TemperatureAndHumiditySensor(17);
    }

    @Bean
    public Sensors sensors() {
	List<Sensor> result = new ArrayList<>();
	result.add(barometricSensor());
	result.add(temperatureAndHumiditySensor());
	return new Sensors(result);
    }

    @Bean
    public ObservableSensor outdoorMotionSensor() {
	// GPIO 18 is P1 - change to whatever the outdoor motion sensor is connected to
	return new GpioObservableSensor(gpio(), "outdoor motion", RaspiPin.GPIO_01);
    }

    @Bean
    public Control outdoorLightControl() {
	// GPIO 27 is P2 - change to whatever the outdoor light control is connected to
	return new GpioControl(gpio(), "outdoor light", RaspiPin.GPIO_02);
    }

    @Bean
    public CameraControl cameraControl() {
	return new MjpegStreamerCameraControl("/etc/init.d/mjpg-streamer start", "/etc/init.d/mjpg-streamer stop", 2);
    }

    @Bean
    public MotionSensorController motionSensorController() {
	return new MotionSensorController(outdoorMotionSensor(), outdoorLightControl());
    }

    @Bean
    public Gpio gpio() {
	try {
	    Gpio result = new GpioImpl(GpioFactory.getInstance());
	    // try to cleanup first
	    result.unexportAll();
	    return result;
	} catch (UnsatisfiedLinkError e) {
	    LOG.error("Unable to create the GPIO controller. Using a mock one instead.", e);
	    return new MockGpio();
	}
    }
}

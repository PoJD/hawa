package cz.pojd.rpi.spring;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;

import cz.pojd.rpi.controllers.ControlObserver;
import cz.pojd.rpi.controls.CameraControl;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.controls.GpioControl;
import cz.pojd.rpi.controls.MjpegStreamerCameraControl;
import cz.pojd.rpi.sensors.Sensor;
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
    public ObservableSensor outdoorMotionSensor() {
	ObservableSensor sensor = new GpioObservableSensor(gpio(), "outdoor motion", RaspiPin.GPIO_01);
	sensor.addObserver(new ControlObserver(outdoorLightControl()));
	return sensor;
    }

    @Bean
    public ObservableSensor hallDownMotionSensor() {
	ObservableSensor sensor = new GpioObservableSensor(gpio(), "hall down motion", RaspiPin.GPIO_02);
	sensor.addObserver(new ControlObserver(hallDownLightControl()));
	return sensor;
    }

    @Bean
    public ObservableSensor hallUpMotionSensor() {
	ObservableSensor sensor = new GpioObservableSensor(gpio(), "hall up motion", RaspiPin.GPIO_03);
	sensor.addObserver(new ControlObserver(hallUpLightControl()));
	return sensor;
    }

    @Bean
    public Control outdoorLightControl() {
	return new GpioControl(gpio(), "outdoor light", RaspiPin.GPIO_04);
    }

    @Bean
    public Control hallDownLightControl() {
	return new GpioControl(gpio(), "hall down light", RaspiPin.GPIO_05);
    }

    @Bean
    public Control hallUpLightControl() {
	return new GpioControl(gpio(), "hall up light", RaspiPin.GPIO_06);
    }

    @Bean
    public CameraControl cameraControl() {
	return new MjpegStreamerCameraControl("/etc/init.d/mjpg-streamer start", "/etc/init.d/mjpg-streamer stop", 2);
    }

    @Bean
    public GpioProvider lightControlsFloor1() {
	return getMCP23017Provider(0x20);
    }

    @Bean
    public GpioProvider lightControlsFloor2() {
	return getMCP23017Provider(0x21);
    }

    public GpioProvider getMCP23017Provider(int address) {
	try {
	    return new MCP23017GpioProvider(rpiConfig.newRasPI() ? I2CBus.BUS_1 : I2CBus.BUS_0, address);
	} catch (IOException | UnsatisfiedLinkError e) {
	    LOG.error("Unable to locate MCP23017 at address " + address + ", using default provider instead.", e);
	    return gpio().getDefaultProvider();
	}
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

package cz.pojd.rpi.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pi4j.io.gpio.GpioFactory;

import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;
import cz.pojd.rpi.sensors.gpio.Dht22Am2302TemperatureAndHumiditySensor;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.gpio.GpioImpl;
import cz.pojd.rpi.sensors.gpio.MockGpio;
import cz.pojd.rpi.sensors.i2c.Bmp180BarometricSensor;
import cz.pojd.rpi.system.RuntimeExecutor;
import cz.pojd.rpi.system.RuntimeExecutorImpl;

/**
 * Main configuration for spring
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
@ComponentScan("cz.pojd.rpi")
public class RpiConfig {
    private static final Log LOG = LogFactory.getLog(RpiConfig.class);

    /**
     * Detects whether this is a new RasPI or not (drives further logic in this project)
     * 
     * @return true if so, false otherwise
     */
    @Bean
    public boolean newRasPI() {
	return true;
    }

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
    public List<String> fileSystems() {
	List<String> fileSystems = new ArrayList<>();
	fileSystems.add("/");
	fileSystems.add("/boot");
	fileSystems.add("/var/log");
	fileSystems.add("/tmp");
	return fileSystems;
    }

    @Bean
    public Runtime runtime() {
	return Runtime.getRuntime();
    }

    @Bean
    public RuntimeExecutor runtimeExecutor() {
	return new RuntimeExecutorImpl();
    }

    @Bean
    public Sensors sensors() {
	List<Sensor> result = new ArrayList<>();
	result.add(new Bmp180BarometricSensor(newRasPI(), altitude()));
	result.add(new Dht22Am2302TemperatureAndHumiditySensor(dht22Am2303Pin()));
	return new Sensors(result);
    }

    /**
     * Detects GPIO pin that the DHT22/AM2302 sensor is connected to
     * 
     * @return Pin holding the Dht sensor pin (following /sys/class naming)
     */
    @Bean
    public int dht22Am2303Pin() {
	return 17;
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

package cz.pojd.rpi.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;

import cz.pojd.rpi.controls.CameraControl;
import cz.pojd.rpi.controls.MjpegStreamerCameraControl;
import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.gpio.GpioImpl;
import cz.pojd.rpi.sensors.gpio.MockGpio;
import cz.pojd.rpi.state.OsStateService;
import cz.pojd.rpi.state.OsStateServiceImpl;
import cz.pojd.rpi.state.VmStateService;
import cz.pojd.rpi.state.VmStateServiceImpl;
import cz.pojd.rpi.system.RuntimeExecutor;
import cz.pojd.rpi.system.RuntimeExecutorImpl;
import cz.pojd.rpi.system.TimeService;
import cz.pojd.rpi.system.TimeServiceImpl;

/**
 * Main configuration for spring in rpi project
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
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

    @Bean
    public List<String> fileSystems() {
	List<String> fileSystems = new ArrayList<>();
	fileSystems.add("/");
	fileSystems.add("/boot");
	fileSystems.add("/var/log");
	fileSystems.add("/tmp");
	return fileSystems;
    }

    /**
     * This command should detect whether the underlying database on the RPi is running or not. This command should either return true or false
     * 
     * @return String command to invoke at runtime to detect whether the DB is running or not. The command should return String 'true', if DB is
     *         running, or 'false' otherwise
     */
    @Bean
    public String detectDbIsRunningCommand() {
	return "if [ `/etc/init.d/mysql status | tail -1 | sed 's/.*: \\(\\w*\\)/\\1/'` = 'started' ]; then echo 'true'; else echo 'false'; fi;";
    }

    @Bean
    public VmStateService vmStateService() {
	return new VmStateServiceImpl();
    }

    @Bean
    public OsStateService osStateService() {
	return new OsStateServiceImpl();
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
    public TimeService timeService() {
	return new TimeServiceImpl();
    }

    @Bean
    public CameraControl cameraControl() {
	return new MjpegStreamerCameraControl("/etc/init.d/mjpg-streamer start", "/etc/init.d/mjpg-streamer stop", 2);
    }

    public GpioProvider getMCP23017Provider(int address) {
	try {
	    return new MCP23017GpioProvider(newRasPI() ? I2CBus.BUS_1 : I2CBus.BUS_0, address);
	} catch (IOException | UnsatisfiedLinkError e) {
	    LOG.error("Unable to locate MCP23017 at address " + String.format("0x%x", address) + ", using default provider instead.", e);
	    return gpio().getDefaultProvider();
	}
    }

    @Bean
    public Gpio gpio() {
	try {
	    // bump the priority of the low level pi4j libs to give the interrupt threads higher precedence
	    return new GpioImpl(GpioFactory.getInstance(), 99);
	} catch (UnsatisfiedLinkError e) {
	    LOG.error("Unable to create the GPIO controller. Using a mock one instead.", e);
	    return new MockGpio();
	}
    }
}

package cz.pojd.rpi.sensors.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioProvider;

/**
 * Custom GPIO interface
 * 
 * @author Lubos Housa
 * @since Aug 4, 2014 9:07:57 PM
 */
public interface Gpio extends GpioController {

    /**
     * Detects whether this is a real GPIO controller talking to real GPIO pins or just a mock one (client code should then not expect real readings)
     * 
     * @return
     */
    public boolean isReal();

    /**
     * Get default provider for GPIO
     * 
     * @return default provider
     */
    public GpioProvider getDefaultProvider();
}

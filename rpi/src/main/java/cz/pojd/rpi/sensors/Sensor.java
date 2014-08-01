package cz.pojd.rpi.sensors;

import java.util.List;

/**
 * Abstraction of a sensor attached to the RPi 
 *
 * @author Lubos Housa
 * @since Aug 1, 2014 9:47:46 PM
 */
public interface Sensor {

    /**
     * Read the current values of this sensor
     * @return list of current values detected by the sensor
     */
    public List<Double> readAll();
    
    /**
     * Read the current value of this sensor (if this sensor returns a single value only)
     * @return current reading of the sensor
     */
    public Double read(); 
}

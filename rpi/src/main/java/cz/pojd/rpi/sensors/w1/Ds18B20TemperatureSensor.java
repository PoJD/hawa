package cz.pojd.rpi.sensors.w1;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.AbstractSensor;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.system.RuntimeExecutor;

/**
 * DS18B20 Temperature digital sensor connected to the RPi via 1-Wire interface. This class needs to know the ID to access the proper sensor (more can
 * be attached to the same 1-Wire interface).
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 9:53:10 PM
 */
public class Ds18B20TemperatureSensor extends AbstractSensor implements Sensor {

    private static final Log LOG = LogFactory.getLog(Ds18B20TemperatureSensor.class);

    private static final String COMMAND = "cat /sys/bus/w1/devices/%ID%/w1_slave | tail -1 | sed 's/.*t=//'";
    private static final Pattern pattern = Pattern.compile("%ID%");

    private RuntimeExecutor runtimeExecutor;
    private String command;
    private String id;

    private boolean fixErrorValues = true;

    /**
     * Id of this sensor. To find out, connect this sensor to RPi as needed and then access the file system to find the respective directory name. For
     * example for a path like <br>
     * <p>
     * /sys/bus/w1/devices/28-0000060a84d1
     * </p>
     * <br>
     * pass
     * <p>
     * 28-0000060a84d1
     * </p>
     * to this constructor
     * 
     * @param runtimeExecutor
     *            runtimeExecutor to use for executing commands at runtime
     * @param id
     *            id of the temperature sensor in the file system
     */
    public Ds18B20TemperatureSensor(RuntimeExecutor runtimeExecutor, String id) {
	this.runtimeExecutor = runtimeExecutor;
	this.command = pattern.matcher(COMMAND).replaceFirst(id);
	this.id = id;
    }

    public RuntimeExecutor getRuntimeExecutor() {
	return runtimeExecutor;
    }

    public void setRuntimeExecutor(RuntimeExecutor runtimeExecutor) {
	this.runtimeExecutor = runtimeExecutor;
    }

    public boolean isFixErrorValues() {
	return fixErrorValues;
    }

    /**
     * Change the behavior of this sensor with regards to reading errorenous values (out of range 1-40 degrees)
     * 
     * @param fixErrorValues
     *            true if errorenous values should not be returned, false otherwise
     */
    public void setFixErrorValues(boolean fixErrorValues) {
	this.fixErrorValues = fixErrorValues;
    }

    @Override
    public List<Reading> readAll() {
	return Collections.singletonList(read());
    }

    @Override
    public Reading read() {
	List<Double> result = getRuntimeExecutor().execute(command);
	if (result.size() == 1) {
	    // the output from command line is 1000 * temperature
	    double temperature = result.get(0) / 1000;
	    if (isFixErrorValues() && (temperature < 1. || temperature > 40.)) {
		LOG.warn("Error temperature read from the sensor '" + id + "': " + temperature + ". Ignoring this output.");
	    } else {
		return Reading.newBuilder().type(Type.temperatureB).doubleValue(temperature).build();
	    }
	}
	return Reading.invalid(Type.temperatureB);
    }

    @Override
    public boolean isInitiated() {
	// no init needed for this sensor really, so we assume all is OK (we will find out till at runtime once someone will attempt reading)
	return true;
    }
}

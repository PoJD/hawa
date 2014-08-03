package cz.pojd.rpi.sensors;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.system.RuntimeExecutor;

/**
 * DS18B20 Temperature digital sensor connected to the RPi via 1-Wire interface. This class needs to know the ID to access the proper sensor (more can
 * be attached to the same 1-Wire interface).
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 9:53:10 PM
 */
public class Ds18B20TemperatureSensor extends AbstractSensor implements Sensor {

    private static final String COMMAND = "cat /sys/bus/w1/devices/%ID%/w1_slave | tail -1 | sed 's/.*t=//'";
    private static final Pattern pattern = Pattern.compile("%ID%");

    private RuntimeExecutor runtimeExecutor;
    private String command;

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
    }

    public RuntimeExecutor getRuntimeExecutor() {
	return runtimeExecutor;
    }

    public void setRuntimeExecutor(RuntimeExecutor runtimeExecutor) {
	this.runtimeExecutor = runtimeExecutor;
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
	    return Reading.newBuilder().type(Type.temperature).value(double2String(result.get(0) / 1000)).build();
	} else {
	    return Reading.unknown(Type.temperature);
	}
    }

}

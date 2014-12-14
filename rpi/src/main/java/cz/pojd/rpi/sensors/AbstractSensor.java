package cz.pojd.rpi.sensors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.Reading.Type;

public abstract class AbstractSensor implements Sensor {
    private static final Log LOG = LogFactory.getLog(AbstractSensor.class);

    protected void waitfor(long howMuch) {
	try {
	    Thread.sleep(howMuch);
	} catch (InterruptedException e) {
	    LOG.error("Interrupted while sleeping...");
	    Thread.currentThread().interrupt();
	}
    }

    protected Reading translateTemperature(double temperature) {
	if (temperature < MIN_TEMPERATURE || temperature > MAX_TEMPERATURE) {
	    LOG.warn("Error temperature read from the sensor '" + this + "': " + temperature + ". Ignoring this output.");
	    return Reading.invalid(Type.temperature);
	} else {
	    return Reading.newBuilder().type(Type.temperature).doubleValue(temperature).units("°C").build();
	}
    }
}

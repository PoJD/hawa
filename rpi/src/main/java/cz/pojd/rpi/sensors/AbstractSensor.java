package cz.pojd.rpi.sensors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.Reading.Type;

public abstract class AbstractSensor implements Sensor {
    private static final Log LOG = LogFactory.getLog(AbstractSensor.class);

    protected Reading translateTemperature(double temperature) {
	return translateValue(temperature, MIN_TEMPERATURE, MAX_TEMPERATURE, Type.temperature, "°C");
    }

    protected Reading translatePressure(double pressure) {
	return translateValue(pressure, MIN_PRESSURE, MAX_PRESSURE, Type.pressure, "HPa");
    }

    private Reading translateValue(double value, double min, double max, Type type, String units) {
	if (value < min || value > max) {
	    LOG.warn("Error value " + value + "read from the sensor '" + this + ". Valid range: (" + min + ", " + max + "). Ignoring this output.");
	    return Reading.invalid(type);
	} else {
	    return Reading.newBuilder().type(type).doubleValue(value).units(units).build();
	}
    }

}

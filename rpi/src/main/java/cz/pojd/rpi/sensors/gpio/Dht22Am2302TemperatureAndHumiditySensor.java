package cz.pojd.rpi.sensors.gpio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.AbstractSensor;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.util.NativeUtils;

/**
 * Sensor implementation for DHT22/AM2302 Digital Temperature And Humidity Sensor. Implemented using Adafruit Python library installed on Pi.
 * https://github.com/adafruit/Adafruit_Python_DHT. This class expects a library with name dht exists on the RasPi (get the libdht.so to e.g.
 * 
 * @author Lubos Housa
 * @since Aug 4, 2014 7:27:57 PM
 */
public class Dht22Am2302TemperatureAndHumiditySensor extends AbstractSensor implements Sensor {

    private static final Log LOG = LogFactory.getLog(Dht22Am2302TemperatureAndHumiditySensor.class);
    private static boolean initiated = true;

    static {
	try {
	    NativeUtils.loadLibraryFromJar("/libdht.so");
	} catch (UnsatisfiedLinkError | IOException e) {
	    LOG.error("Unable to load the shared library dht", e);
	    initiated = false;
	}
    }

    private final int gpioPin;

    /**
     * Create new instance of this sensor
     * 
     * @param gpioPin
     *            gpioPin that the Dht sensor is connected to
     */
    public Dht22Am2302TemperatureAndHumiditySensor(int gpioPin) {
	this.gpioPin = gpioPin;
    }

    @Override
    public List<Reading> readAll() {
	List<Reading> result = new ArrayList<>();
	if (initiated) {
	    double[] numbers = executeNative(gpioPin);
	    if (numbers != null && numbers.length == 2) {
		result.add(Reading.newBuilder().type(Type.temperatureDht).value(double2String(numbers[0]) + "°C").build());
		result.add(Reading.newBuilder().type(Type.humidity).value(double2String(numbers[1]) + "%").build());
	    } else {
		LOG.warn("Some error ocurred when attempting to read the values from the sensor.");
	    }
	} else {
	    LOG.warn("Not attempting to read anything from the sensor given the library load failed before.");
	}
	return result;
    }

    @Override
    public Reading read() {
	List<Reading> result = readAll();
	if (result.size() == 2) {
	    return result.get(0);
	} else {
	    return Reading.unknown(Type.temperatureDht);
	}
    }

    /**
     * Invoke the method via JNI
     * 
     * @return temperature and humidity, in this order
     * @param gpioPin
     *            pin number
     * @return
     */
    private native double[] executeNative(int gpioPin);
}

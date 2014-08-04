package cz.pojd.rpi.sensors.gpio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;

import cz.pojd.rpi.sensors.AbstractSensor;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Sensor implementation for DHT22/AM2302 Digital Temperature And Humidity Sensor. Pretty much rewritten driver from <a
 * href="https://github.com/adafruit/Adafruit_Python_DHT/blob/master/source/Raspberry_Pi/pi_dht_read.c"
 * >https://github.com/adafruit/Adafruit_Python_DHT/blob/master/source/Raspberry_Pi/pi_dht_read.c</a> to java
 *  
 * @author Lubos Housa
 * @since Aug 4, 2014 7:27:57 PM
 */
public class Dht22Am2302TemperatureAndHumiditySensor extends AbstractSensor implements Sensor {

    private static final Log LOG = LogFactory.getLog(Dht22Am2302TemperatureAndHumiditySensor.class);

    /**
     * This is the only processor specific magic value, the maximum amount of time to spin in a loop before bailing out and considering the read a
     * timeout. This should be a high value, but if you're running on a much faster platform than a Raspberry Pi or Beaglebone Black then it
     * might need to be increased.
     */
    private static final int DHT_MAXCOUNT = 32000;

    /**
     * Number of bit pulses to expect from the DHT. Note that this is 41 because /* the first pulse is a constant 50 microsecond pulse, with 40 pulses
     * to represent /* the data afterwards.
     * 
     */
    private static final int DHT_PULSES = 41;

    private GpioPinDigitalMultipurpose pin;
    private Gpio gpio;

    public Dht22Am2302TemperatureAndHumiditySensor(Gpio gpio, Pin pin) {
	LOG.info("Creating instance of Dht22Am2302TemperatureAndHumiditySensor (uses Gpio). Gpio is " + (gpio.isReal() ? "" : "not") + " real.");
	this.gpio = gpio;

	try {
	    this.pin = gpio.provisionDigitalMultipurposePin(pin, PinMode.DIGITAL_OUTPUT);
	} catch (Exception e) {
	    LOG.error("Unable to provision the pin for this sensor", e);
	}
    }

    private String getFriendlyName() {
	return "DHT22/AM2302 Digital Temperature And Humidity Sensor";
    }

    private int[] readPulses() {
	// Store the count that each DHT bit pulse is low and high. Make sure array is initialized to start at zero.
	int pulseCounts[] = new int[DHT_PULSES * 2];

	// Set pin to output first
	pin.setMode(PinMode.DIGITAL_OUTPUT);
	// Set pin high for ~500 milliseconds.
	pin.high();
	waitfor(500);

	// The next calls are timing critical and care should be taken
	// to ensure no unnecessary work is done below.
	// Set pin low for ~20 milliseconds.
	pin.low();
	waitfor(20);

	// Set pin at input.
	pin.setMode(PinMode.DIGITAL_INPUT);
	// Need a very short delay before reading pins or else value is sometimes still low.
	for (int i=0; i<30; i++);

	// Wait for DHT to pull pin low.
	for (int count = 0; pin.isHigh(); count++) {
	    if (count >= DHT_MAXCOUNT) {
		LOG.error("Timeout waiting for " + getFriendlyName() + " input to become low.");
		return null;
	    }
	}

	// Record pulse widths for the expected result bits.
	for (int i = 0; i < DHT_PULSES * 2; i += 2) {
	    // Count how long pin is low and store in pulseCounts[i]
	    while (pin.isLow()) {
		if (++pulseCounts[i] >= DHT_MAXCOUNT) {
		    // Timeout waiting for response.
		    LOG.error("Timeout waiting for " + getFriendlyName() + " input to become low at pulse " + i);
		    return null;
		}
	    }
	    // Count how long pin is high and store in pulseCounts[i+1]
	    while (pin.isHigh()) {
		if (++pulseCounts[i + 1] >= DHT_MAXCOUNT) {
		    // Timeout waiting for response.
		    LOG.error("Timeout waiting for " + getFriendlyName() + " input to become high at pulse " + (i + 1));
		    return null;
		}
	    }
	}
	return pulseCounts;
    }

    private void calculateResults(List<Reading> result, int[] pulseCounts) {
	// Compute the average low pulse width to use as a 50 microsecond reference threshold.
	// Ignore the first two readings because they are a constant 80 microsecond pulse.
	int threshold = 0;
	for (int i = 2; i < DHT_PULSES * 2; i += 2) {
	    threshold += pulseCounts[i];
	}
	threshold /= DHT_PULSES - 1;

	// Interpret each high pulse as a 0 or 1 by comparing it to the 50us reference.
	// If the count is less than 50us it must be a ~28us 0 pulse, and if it's higher
	// then it must be a ~70us 1 pulse.
	int data[] = new int[5];
	for (int i = 3; i < DHT_PULSES * 2; i += 2) {
	    int index = (i - 3) / 16;
	    data[index] <<= 1;
	    if (pulseCounts[i] >= threshold) {
		// One bit for long pulse.
		data[index] |= 1;
	    }
	    // Else zero bit for short pulse.
	}

	if (LOG.isDebugEnabled()) {
	    LOG.debug("data[0]=" + data[0] + ",data[1]=" + data[1] + ",data[2]=" + data[2] + ",data[3]=" + data[3] + ",data[4]=" + data[4]);
	}

	// Verify checksum of received data.
	if (data[4] == ((data[0] + data[1] + data[2] + data[3]) & 0xFF)) {
	    // Calculate humidity and temp for DHT22 sensor.
	    result.add(Reading.newBuilder().type(Type.humidity).value(double2String((data[0] * 256 + data[1]) / 10.) + "%").build());
	    double temperature = ((data[2] & 0x7F) * 256 + data[3]) / 10.;
	    if ((data[2] & 0x80) > 0) {
		temperature *= -1.;
	    }
	    result.add(Reading.newBuilder().type(Type.temperatureD).value(double2String(temperature) + "°C").build());
	} else {
	    LOG.error("Checksum failed. data[0]=" + data[0] + ",data[1]=" + data[1] + ",data[2]=" + data[2] + ",data[3]=" + data[3] + ",data[4]="
		    + data[4]);
	}
    }

    @Override
    public synchronized List<Reading> readAll() {
	List<Reading> result = new ArrayList<>();
	if (!gpio.isReal() || pin == null) {
	    LOG.warn("Not attempting to read anything from the sensor as there is no real Gpio interface to communicate through (or the init failed).");
	    return result;
	}

	try {
	    int[] pulseCounts = readPulses();
	    if (pulseCounts != null) {
		calculateResults(result, pulseCounts);
	    }
	} catch (Exception e) {
	    LOG.error("Error reading via GPIO, returning the intermediate result.", e);
	}
	return result;
    }

    @Override
    public Reading read() {
	// simply return just the temperature if someone wants 1 value only
	List<Reading> all = readAll();
	if (all.size() == 2) {
	    return all.get(0);
	} else {
	    return Reading.unknown(Type.temperatureD);
	}
    }
}

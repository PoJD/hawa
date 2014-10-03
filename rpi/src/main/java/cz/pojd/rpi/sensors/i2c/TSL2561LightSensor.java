package cz.pojd.rpi.sensors.i2c;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Reading.Type;

/**
 * Light Sensor (I2C). The code taken from https://code.google.com/p/raspberry-pi4j-samples/ and changed to accomodate the project requirements and
 * API
 * 
 * @see https://code.google.com/p/raspberry-pi4j-samples/
 */
public class TSL2561LightSensor extends I2CSensor implements Sensor, DisposableBean {

    private static final Log LOG = LogFactory.getLog(TSL2561LightSensor.class);

    public final static int TSL2561_ADDRESS_LOW = 0x29; // address pin connected to ground
    public final static int TSL2561_ADDRESS_FLOAT = 0x39; // nothing attached to address pin
    public final static int TSL2561_ADDRESS_HIGH = 0x49; // address pin connected to 3.3v

    private final static int TSL2561_COMMAND_BIT = 0x80;
    private final static byte TSL2561_CONTROL_POWERON = 0x03;
    private final static byte TSL2561_CONTROL_POWEROFF = 0x00;

    private final static int TSL2561_REGISTER_CHAN0_LOW = 0x0C;
    private final static int TSL2561_REGISTER_CHAN1_LOW = 0x0E;

    private final static double TSL2561_LUX_B1C = 0.0315; // (0x0204) // 0.0315 * 2^LUX_SCALE
    private final static double TSL2561_LUX_K4C = 0.520; // (0x010a) // 0.520 * 2^RATIO_SCALE
    private final static double TSL2561_LUX_K5C = 0.65; // (0x014d) // 0.65 * 2^RATIO_SCALE
    private final static double TSL2561_LUX_B5C = 0.0229; // (0x0177) // 0.0229 * 2^LUX_SCALE
    private final static double TSL2561_LUX_M5C = 0.0291; // (0x01dd) // 0.0291 * 2^LUX_SCALE
    private final static double TSL2561_LUX_K6C = 0.80; // (0x019a) // 0.80 * 2^RATIO_SCALE
    private final static double TSL2561_LUX_B6C = 0.0157; // (0x0101) // 0.0157 * 2^LUX_SCALE
    private final static double TSL2561_LUX_M6C = 0.0180; // (0x0127) // 0.0180 * 2^LUX_SCALE
    private final static double TSL2561_LUX_K7C = 1.3; // (0x029a) // 1.3 * 2^RATIO_SCALE
    private final static double TSL2561_LUX_B7C = 0.00338; // (0x0037) // 0.00338 * 2^LUX_SCALE
    private final static double TSL2561_LUX_M7C = 0.00260; // (0x002b) // 0.00260 * 2^LUX_SCALE
    private final static double TSL2561_LUX_K8C = 1.3; // (0x029a) // 1.3 * 2^RATIO_SCALE

    public TSL2561LightSensor(boolean newRaspi, int address) {
	super(newRaspi, address, true);
    }

    @Override
    protected void setupSensor() throws IOException {
	turnOn();
    }

    private void turnOn() throws IOException {
	device.write(TSL2561_COMMAND_BIT, TSL2561_CONTROL_POWERON);
    }

    private void turnOff() throws IOException {
	device.write(TSL2561_COMMAND_BIT, TSL2561_CONTROL_POWEROFF);
    }

    /**
     * Reads visible+IR diode from the I2C device
     */
    private int readFull() throws IOException {
	int reg = TSL2561_COMMAND_BIT | TSL2561_REGISTER_CHAN0_LOW;
	return readU16(reg);
    }

    /**
     * Reads IR only diode from the I2C device
     */
    private int readIR() throws IOException {
	int reg = TSL2561_COMMAND_BIT | TSL2561_REGISTER_CHAN1_LOW;
	return readU16(reg);
    }

    /**
     * Device lux range 0.1 - 40,000+ see https://learn.adafruit.com/tsl2561/overview
     */
    private double readLux() throws IOException {
	double ambient = readFull();
	double ir = readIR();

	if (ambient >= 0xffff || ir >= 0xffff) // value(s) exceed(s) datarange
	    throw new IOException("Gain too high. Values exceed range.");

	double ratio = ir / ambient;

	if (LOG.isDebugEnabled()) {
	    LOG.debug("IR Result:" + ir);
	    LOG.debug("Ambient Result:" + ambient);
	}
	/*
	 * For the values below, see https://github.com/adafruit/Adafruit_TSL2561/blob/master/Adafruit_TSL2561_U.h
	 */
	double lux = 0d;
	if ((ratio >= 0) && (ratio <= TSL2561_LUX_K4C))
	    lux = (TSL2561_LUX_B1C * ambient) - (0.0593 * ambient * (Math.pow(ratio, 1.4)));
	else if (ratio <= TSL2561_LUX_K5C)
	    lux = (TSL2561_LUX_B5C * ambient) - (TSL2561_LUX_M5C * ir);
	else if (ratio <= TSL2561_LUX_K6C)
	    lux = (TSL2561_LUX_B6C * ambient) - (TSL2561_LUX_M6C * ir);
	else if (ratio <= TSL2561_LUX_K7C)
	    lux = (TSL2561_LUX_B7C * ambient) - (TSL2561_LUX_M7C * ir);
	else if (ratio > TSL2561_LUX_K8C)
	    lux = 0;

	return lux;
    }
    
    @Override
    public List<Reading> readAll() {
	List<Reading> result = new ArrayList<>();
	try {
	    if (isInitiated()) {
		result.add(Reading.newBuilder().type(Type.light).doubleValue(readLux()).units(" lux").build());
	    } else {
		LOG.warn("Init failed before, not attempting to read anything from the sensor.");
	    }
	} catch (IOException e) {
	    LOG.error("Unable to read the output of the sensor", e);
	}
	return result;
    }

    @Override
    public Reading read() {
	List<Reading> all = readAll();
	if (all.size() == 1) {
	    return all.get(0);
	} else {
	    return Reading.invalid(Type.light);
	}
    }

    @Override
    public void destroy() throws Exception {
	LOG.info("Turning off...");
	turnOff();
    }
}

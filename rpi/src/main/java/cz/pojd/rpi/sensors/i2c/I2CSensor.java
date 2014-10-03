package cz.pojd.rpi.sensors.i2c;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import cz.pojd.rpi.sensors.AbstractSensor;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Abstract I2C Sensor.
 * 
 * @author Lubos Housa
 * @since Aug 2, 2014 12:34:56 AM
 */
public abstract class I2CSensor extends AbstractSensor implements Sensor {

    private static final Log LOG = LogFactory.getLog(I2CSensor.class);

    private I2CBus bus;
    private boolean initiated;
    private int deviceAddress;
    private boolean bigEndian;

    protected I2CDevice device;

    public I2CSensor(boolean newRasPI, int deviceAddress, boolean bigEndian) {
	LOG.info("Attempt to connect to I2C sensor. newRasPI=" + newRasPI + ". deviceAddress=" + toHex(deviceAddress) + ". bigEndian=" + bigEndian);
	this.deviceAddress = deviceAddress;

	try {
	    // Get i2c bus
	    bus = I2CFactory.getInstance(newRasPI ? I2CBus.BUS_1 : I2CBus.BUS_0);
	    if (LOG.isInfoEnabled())
		LOG.info("Connected to I2C Bus. OK.");

	    // Get device itself
	    device = bus.getDevice(deviceAddress);
	    if (LOG.isInfoEnabled())
		LOG.info("Connected to I2C Device. OK.");

	    initiated = true;
	} catch (Exception | UnsatisfiedLinkError e) {
	    // UnsatisfiedLinkError is caught here too to be able to start this up on any device, not just RasPi
	    LOG.error("Error connecting to the device via I2C", e);
	}

	if (isInitiated()) {
	    try {
		setupSensor();
	    } catch (Exception e) {
		LOG.error("Error setting up the device", e);
		initiated = false;
	    }
	}
    }

    /**
     * Sets up the sensor. This logic is invoked once only after successful connection to the I2C Bus and connecting to the device. Typical use would
     * be reading some calibration data, etc.
     */
    protected abstract void setupSensor() throws Exception;

    /**
     * Read an unsigned byte from the I2C device
     * 
     * @param reg
     * @return
     */
    private int readU8(int reg) {
	int result = 0;
	try {
	    result = device.read(reg);
	    if (LOG.isDebugEnabled())
		LOG.debug("I2C: Device " + toHex(deviceAddress) + " returned " + result + " from reg " + reg);
	} catch (Exception e) {
	    LOG.error("Error reading byte from the device", e);
	}
	return result;
    }

    /**
     * Reads a signed byte from the I2C device
     * 
     * @param reg
     * @return
     */
    protected int readS8(int reg) {
	int result = readU8(reg);
	if (result > 127) {
	    result -= 256;
	}
	return result;
    }

    protected int readU16(int register) {
	int hi = readU8(register);
	int lo = readU8(register + 1);
	return bigEndian ? (lo << 8) + hi : (hi << 8) + lo;
    }

    protected int readS16(int register) {
	int hi = readS8(register);
	int lo = readU8(register + 1);
	return (hi << 8) + lo;
    }

    private String toHex(int address) {
	return String.format("0x%x", address);
    }

    @Override
    public boolean isInitiated() {
	return initiated;
    }
}

package cz.pojd.rpi.sensors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import cz.pojd.rpi.sensors.Reading.Type;

/**
 * 
 * Barometric sensor. The code below is pretty much a copy paste from
 * https://code.google.com/p/raspberry-pi4j-samples/source/browse/AdafruitI2C/src/adafruiti2c/sensor/nmea/AdafruitBMP180Reader.java?edit=1 adjusted a
 * bit.
 * 
 * Reads temperature and pressure at sea level in this order.
 * 
 * @author Lubos Housa
 * @since Aug 2, 2014 12:34:56 AM
 */
public class Bmp180BarometricSensor extends AbstractSensor implements Sensor {

    private static final Log LOG = LogFactory.getLog(Bmp180BarometricSensor.class);

    private final static int BMP180_ADDRESS = 0x77;
    // Operating Modes
    private final static int BMP180_ULTRALOWPOWER = 0;
    private final static int BMP180_HIGHRES = 2;
    private final static int BMP180_ULTRAHIGHRES = 3;

    // BMP085 Registers
    private final static int BMP180_CAL_AC1 = 0xAA; // R Calibration data (16 bits)
    private final static int BMP180_CAL_AC2 = 0xAC; // R Calibration data (16 bits)
    private final static int BMP180_CAL_AC3 = 0xAE; // R Calibration data (16 bits)
    private final static int BMP180_CAL_AC4 = 0xB0; // R Calibration data (16 bits)
    private final static int BMP180_CAL_AC5 = 0xB2; // R Calibration data (16 bits)
    private final static int BMP180_CAL_AC6 = 0xB4; // R Calibration data (16 bits)
    private final static int BMP180_CAL_B1 = 0xB6; // R Calibration data (16 bits)
    private final static int BMP180_CAL_B2 = 0xB8; // R Calibration data (16 bits)
    private final static int BMP180_CAL_MB = 0xBA; // R Calibration data (16 bits)
    private final static int BMP180_CAL_MC = 0xBC; // R Calibration data (16 bits)
    private final static int BMP180_CAL_MD = 0xBE; // R Calibration data (16 bits)
    private final static int BMP180_CONTROL = 0xF4;
    private final static int BMP180_TEMPDATA = 0xF6;
    private final static int BMP180_PRESSUREDATA = 0xF6;
    private final static int BMP180_READTEMPCMD = 0x2E;
    private final static int BMP180_READPRESSURECMD = 0x34;

    private int cal_AC1 = 0;
    private int cal_AC2 = 0;
    private int cal_AC3 = 0;
    private int cal_AC4 = 0;
    private int cal_AC5 = 0;
    private int cal_AC6 = 0;
    private int cal_B1 = 0;
    private int cal_B2 = 0;
    private int cal_MB = 0;
    private int cal_MC = 0;
    private int cal_MD = 0;

    private I2CBus bus;
    private I2CDevice bmp180;
    private int mode = BMP180_ULTRAHIGHRES;

    private boolean initiated;
    private int altitude;

    @Inject
    public Bmp180BarometricSensor(boolean newRasPI, int altitude) {
	LOG.info("Attempt to start up the barometric sensor. newRasPI=" + newRasPI + ". altitude=" + altitude);
	this.altitude = altitude;

	try {
	    // Get i2c bus
	    bus = I2CFactory.getInstance(newRasPI ? I2CBus.BUS_1 : I2CBus.BUS_0);
	    if (LOG.isInfoEnabled())
		LOG.info("Connected to I2C Bus. OK.");

	    // Get device itself
	    bmp180 = bus.getDevice(BMP180_ADDRESS);
	    if (LOG.isInfoEnabled())
		LOG.info("Connected to I2C Device. OK.");

	    initiated = true;
	} catch (Exception | UnsatisfiedLinkError e) {
	    // UnsatisfiedLinkError is caught here too to be able to start this up on any device, not just RasPi
	    LOG.error("Error connecting to the device via I2C", e);
	}

	if (initiated) {
	    try {
		readCalibrationData();
	    } catch (Exception e) {
		LOG.error("Error reading calibration data from the device", e);
	    }
	}
    }

    /**
     * Read an unsigned byte from the I2C device
     * 
     * @param reg
     * @return
     */
    private int readU8(int reg) {
	int result = 0;
	try {
	    result = bmp180.read(reg);
	    if (LOG.isDebugEnabled())
		LOG.debug("I2C: Device " + BMP180_ADDRESS + " returned " + result + " from reg " + reg);
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
    private int readS8(int reg) {
	int result = readU8(reg);
	if (result > 127) {
	    result -= 256;
	}
	return result;
    }

    private int readU16(int register) {
	int hi = readU8(register);
	int lo = readU8(register + 1);
	return (hi << 8) + lo;
    }

    private int readS16(int register) {
	int hi = readS8(register);
	int lo = readU8(register + 1);
	return (hi << 8) + lo;
    }

    /**
     * Reads the calibration data from the IC
     */
    private void readCalibrationData() {
	cal_AC1 = readS16(BMP180_CAL_AC1); // INT16
	cal_AC2 = readS16(BMP180_CAL_AC2); // INT16
	cal_AC3 = readS16(BMP180_CAL_AC3); // INT16
	cal_AC4 = readU16(BMP180_CAL_AC4); // UINT16
	cal_AC5 = readU16(BMP180_CAL_AC5); // UINT16
	cal_AC6 = readU16(BMP180_CAL_AC6); // UINT16
	cal_B1 = readS16(BMP180_CAL_B1); // INT16
	cal_B2 = readS16(BMP180_CAL_B2); // INT16
	cal_MB = readS16(BMP180_CAL_MB); // INT16
	cal_MC = readS16(BMP180_CAL_MC); // INT16
	cal_MD = readS16(BMP180_CAL_MD); // INT16
	if (LOG.isDebugEnabled())
	    showCalibrationData();
    }

    /**
     * Displays the calibration values for debugging purposes
     */
    private void showCalibrationData() {
	LOG.debug("AC1 = " + cal_AC1);
	LOG.debug("AC2 = " + cal_AC2);
	LOG.debug("AC3 = " + cal_AC3);
	LOG.debug("AC4 = " + cal_AC4);
	LOG.debug("AC5 = " + cal_AC5);
	LOG.debug("AC6 = " + cal_AC6);
	LOG.debug("B1  = " + cal_B1);
	LOG.debug("B2  = " + cal_B2);
	LOG.debug("MB  = " + cal_MB);
	LOG.debug("MC  = " + cal_MC);
	LOG.debug("MD  = " + cal_MD);
    }

    /**
     * Reads the raw (uncompensated) temperature from the sensor
     * 
     * @return
     * @throws IOException
     */
    private int readRawTemp() throws IOException {
	bmp180.write(BMP180_CONTROL, (byte) BMP180_READTEMPCMD);
	waitfor(5); // Wait 5ms
	int raw = readU16(BMP180_TEMPDATA);
	if (LOG.isDebugEnabled())
	    LOG.debug("Raw Temp: " + (raw & 0xFFFF) + ", " + raw);
	return raw;
    }

    /**
     * Reads the raw (uncompensated) pressure level from the sensor
     * 
     * @return
     * @throws IOException
     */
    private int readRawPressure() throws IOException {
	bmp180.write(BMP180_CONTROL, (byte) (BMP180_READPRESSURECMD + (mode << 6)));
	if (mode == BMP180_ULTRALOWPOWER)
	    waitfor(5);
	else if (mode == BMP180_HIGHRES)
	    waitfor(14);
	else if (mode == BMP180_ULTRAHIGHRES)
	    waitfor(26);
	else
	    waitfor(8);
	int msb = bmp180.read(BMP180_PRESSUREDATA);
	int lsb = bmp180.read(BMP180_PRESSUREDATA + 1);
	int xlsb = bmp180.read(BMP180_PRESSUREDATA + 2);
	int raw = ((msb << 16) + (lsb << 8) + xlsb) >> (8 - mode);
	if (LOG.isDebugEnabled())
	    LOG.debug("Raw Pressure: " + (raw & 0xFFFF) + ", " + raw);
	return raw;
    }

    /**
     * Gets the compensated temperature in degrees celcius
     * 
     * @return
     * @throws IOException
     */
    private double readTemperature() throws IOException {
	int UT = 0;
	int X1 = 0;
	int X2 = 0;
	int B5 = 0;
	double temp = 0.0f;

	// Read raw temp before aligning it with the calibration values
	UT = readRawTemp();
	X1 = ((UT - cal_AC6) * cal_AC5) >> 15;
	X2 = (cal_MC << 11) / (X1 + cal_MD);
	B5 = X1 + X2;
	temp = ((B5 + 8) >> 4) / 10.0f;
	if (LOG.isDebugEnabled())
	    LOG.debug("Calibrated temperature = " + temp + " C");
	return temp;
    }

    /**
     * Gets the compensated pressure in pascal
     * 
     * @return
     * @throws IOException
     */
    private double readPressure() throws IOException {
	int UT = 0;
	int UP = 0;
	int B3 = 0;
	int B5 = 0;
	int B6 = 0;
	int X1 = 0;
	int X2 = 0;
	int X3 = 0;
	int p = 0;
	int B4 = 0;
	int B7 = 0;

	UT = readRawTemp();
	UP = readRawPressure();
	
	// True Temperature Calculations
	X1 = (int) ((UT - cal_AC6) * cal_AC5) >> 15;
	X2 = (cal_MC << 11) / (X1 + cal_MD);
	B5 = X1 + X2;
	if (LOG.isDebugEnabled()) {
	    LOG.debug("X1 = " + X1);
	    LOG.debug("X2 = " + X2);
	    LOG.debug("B5 = " + B5);
	    LOG.debug("True Temperature = " + (((B5 + 8) >> 4) / 10.0) + " C");
	}

	// Pressure Calculations
	B6 = B5 - 4000;
	X1 = (cal_B2 * (B6 * B6) >> 12) >> 11;
	X2 = (cal_AC2 * B6) >> 11;
	X3 = X1 + X2;
	B3 = (((cal_AC1 * 4 + X3) << mode) + 2) / 4;
	if (LOG.isDebugEnabled()) {
	    LOG.debug("B6 = " + B6);
	    LOG.debug("X1 = " + X1);
	    LOG.debug("X2 = " + X2);
	    LOG.debug("X3 = " + X3);
	    LOG.debug("B3 = " + B3);
	}
		    
	X1 = (cal_AC3 * B6) >> 13;
	X2 = (cal_B1 * ((B6 * B6) >> 12)) >> 16;
	X3 = ((X1 + X2) + 2) >> 2;
	B4 = (cal_AC4 * (X3 + 32768)) >> 15;
	B7 = (UP - B3) * (50000 >> mode);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("X1 = " + X1);
	    LOG.debug("X2 = " + X2);
	    LOG.debug("X3 = " + X3);
	    LOG.debug("B4 = " + B4);
	    LOG.debug("B7 = " + B7);
	}
	if (B7 < 0x80000000)
	    p = (B7 * 2) / B4;
	else
	    p = (B7 / B4) * 2;

	if (LOG.isDebugEnabled())
	    LOG.debug("X1 = " + X1);

	X1 = (p >> 8) * (p >> 8);
	X1 = (X1 * 3038) >> 16;
	X2 = (-7357 * p) >> 16;
	if (LOG.isDebugEnabled()) {
	    LOG.debug("p  = " + p);
	    LOG.debug("X1 = " + X1);
	    LOG.debug("X2 = " + X2);
	}
	p = p + ((X1 + X2 + 3791) >> 4);
	if (LOG.isDebugEnabled())
	    LOG.debug("Pressure = " + p + " Pa");

	return p;
    }

    /**
     * Read pressure as if we were at the sea level and round down to HPa
     * 
     * @return HPa value at the sea level of the current pressure
     * @throws IOException
     */
    private double readPressureHPaAtSeaLevel() throws IOException {
	double p = readPressure();
	// positive altitude / 100 should increase pressure with 1200 - then divide all by 100 to get HPa
	return (p + 1200. * (altitude / 100.)) / 100.;
    }

    private static void waitfor(long howMuch) {
	try {
	    Thread.sleep(howMuch);
	} catch (InterruptedException e) {
	    LOG.error("Interrupted while sleeping...");
	}
    }

    @Override
    public List<Reading> readAll() {
	List<Reading> result = new ArrayList<>();
	try {
	    if (initiated) {
		result.add(Reading.newBuilder().type(Type.temperature).value(double2String(readTemperature()) + "°C").build());
		result.add(Reading.newBuilder().type(Type.pressure).value(double2String(readPressureHPaAtSeaLevel()) + "HPa").build());
	    } else {
		LOG.warn("Init failed before, not attempting to read any output from the sensor.");
	    }
	} catch (IOException e) {
	    LOG.error("Unable to read the output of the sensor", e);
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
	    return Reading.unknown(Type.temperature);
	}
    }

    /**
     * Detects whether this sensor is properly initiated or not
     * 
     * @return true if so, false otherwise
     */
    public boolean isInitiated() {
	return initiated;
    }
}

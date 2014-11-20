package cz.pojd.rpi.sensors.spi;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.gpio.Gpio;

/**
 * MCP3008 wrapper to read outputs from analog sensors. Original implementation here:
 * https://code.google.com/p/raspberry-pi4j-samples/source/browse/ADC/src/analogdigitalconverter/mcp3008/MCP3008Reader.java
 * 
 * This sensor simply reads values between 0-1023. It is up to the requester to interpret that value
 *
 * @author Lubos Housa
 * @since Nov 19, 2014 11:17:10 PM
 */
public class MCP3008Adc implements Sensor {
    private static final Log LOG = LogFactory.getLog(MCP3008Adc.class);

    // see more here: https://projects.drogon.net/raspberry-pi/wiringpi/pins/
    private static final Pin spiClk = RaspiPin.GPIO_14;
    private static final Pin spiMiso = RaspiPin.GPIO_13;
    private static final Pin spiMosi = RaspiPin.GPIO_12;
    private static final Pin spiCs0 = RaspiPin.GPIO_10;
    private static final Pin spiCs1 = RaspiPin.GPIO_11;

    public enum InputChannel {
	CH0(0), CH1(1), CH2(2), CH3(3), CH4(4), CH5(5), CH6(6), CH7(7);

	private final int channel;

	private InputChannel(int chNum) {
	    this.channel = chNum;
	}
    }

    public enum CsChannel {
	CS0(spiCs0), CS1(spiCs1);

	private final Pin pin;

	private CsChannel(Pin pin) {
	    this.pin = pin;
	}
    }

    private final int channel;
    private final Reading.Type readingType;
    private final String units;

    private GpioPinDigitalInput misoInput;
    private GpioPinDigitalOutput mosiOutput;
    private GpioPinDigitalOutput clockOutput;
    private GpioPinDigitalOutput chipSelectOutput;
    private boolean initiated;

    @Inject
    public MCP3008Adc(Gpio gpio, CsChannel csChannel, InputChannel channel, Reading.Type readingType, String units) {
	LOG.info("Initializing analog sensor through MCP3008 at csChannel '" + csChannel + "', input channel '" + channel + "', readingType '"
		+ readingType + "', units '" + units + "'");
	this.channel = channel.channel;
	this.readingType = readingType;
	this.units = units;
	try {
	    this.mosiOutput = gpio.provisionDigitalOutputPin(spiMosi, "MOSI", PinState.LOW);
	    this.clockOutput = gpio.provisionDigitalOutputPin(spiClk, "CLK", PinState.LOW);
	    this.chipSelectOutput = gpio.provisionDigitalOutputPin(csChannel.pin, "CS", PinState.LOW);

	    this.misoInput = gpio.provisionDigitalInputPin(spiMiso, "MISO");
	    this.initiated = mosiOutput != null && clockOutput != null && chipSelectOutput != null && misoInput != null;
	    LOG.info("Initializing of analog sensor through MCP3008 finished "
		    + (initiated ? "OK." : "with issues, some inputs/outputs failed to init."));
	} catch (Exception e) {
	    LOG.error("Unable to initialize analog sensor reader through MCP3008.", e);
	    this.initiated = false;
	}
    }

    private int readAnalog() {
	try {
	    chipSelectOutput.high();

	    clockOutput.low();
	    chipSelectOutput.low();

	    int adccommand = channel;
	    if (LOG.isDebugEnabled()) {
		LOG.debug("1 - ADCCOMMAND: " + toBinary(adccommand));
	    }
	    adccommand |= 0x18; // 0x18: 00011000
	    if (LOG.isDebugEnabled()) {
		LOG.debug("2 - ADCCOMMAND: " + toBinary(adccommand));
	    }
	    adccommand <<= 3;
	    if (LOG.isDebugEnabled()) {
		LOG.debug("3 - ADCCOMMAND: " + toBinary(adccommand));
	    }
	    // Send 5 bits: 8 - 3. 8 input channels on the MCP3008.
	    for (int i = 0; i < 5; i++) {
		if (LOG.isDebugEnabled()) {
		    LOG.debug("4 - (i=" + i + ") ADCCOMMAND: " + toBinary(adccommand));
		}
		if ((adccommand & 0x80) != 0x0) { // 0x80 = 0&10000000
		    mosiOutput.high();
		} else {
		    mosiOutput.low();
		}
		adccommand <<= 1;
		tickPin(clockOutput);
	    }

	    int adcOut = 0;
	    for (int i = 0; i < 12; i++) { // Read in one empty bit, one null bit and 10 ADC bits
		tickPin(clockOutput);
		adcOut <<= 1;

		if (misoInput.isHigh()) {
		    adcOut |= 0x1;
		}
		if (LOG.isDebugEnabled()) {
		    LOG.debug("ADCOUT after cycle " + i + ": " + toBinary(adcOut));
		}
	    }

	    adcOut >>= 1; // Drop first bit

	    if (LOG.isDebugEnabled()) {
		LOG.debug("Result: " + adcOut);
	    }

	    return adcOut;
	} finally {
	    chipSelectOutput.high();
	}
    }

    private void tickPin(GpioPinDigitalOutput pin) {
	pin.high();
	pin.low();
    }

    private String toBinary(int number) {
	return String.format("%16s", Integer.toBinaryString(number)).replace(' ', '0');
    }

    @Override
    public List<Reading> readAll() {
	return Collections.singletonList(read());
    }

    @Override
    public Reading read() {
	try {
	    if (isInitiated()) {
		return Reading.newBuilder().type(readingType).doubleValue(readAnalog()).units(units).build();
	    } else {
		LOG.warn("Init failed before, not attempting to read anything from the sensor through MCP3008.");
	    }
	} catch (Exception e) {
	    LOG.error("Unable to read the output of the analog sensor through MCP3008", e);
	}
	return Reading.invalid(readingType);
    }

    @Override
    public boolean isInitiated() {
	return initiated;
    }
}

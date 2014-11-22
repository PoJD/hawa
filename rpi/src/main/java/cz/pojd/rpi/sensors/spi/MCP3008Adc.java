package cz.pojd.rpi.sensors.spi;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.spi.SpiDevice.InputChannel;
import cz.pojd.rpi.sensors.spi.SpiDevice.SpiChannel;

/**
 * MCP3008 wrapper to read outputs from analog sensors. Uses SPI API to retrieve the values.
 * 
 * Original inspiration (not for hardware SPI though) here:
 * https://code.google.com/p/raspberry-pi4j-samples/source/browse/ADC/src/analogdigitalconverter/mcp3008/MCP3008Reader.java
 * 
 * This sensor simply reads values between 0-1023. It is up to the requester to interpret that value
 *
 * @author Lubos Housa
 * @since Nov 19, 2014 11:17:10 PM
 */
public class MCP3008Adc implements Sensor {
    private static final Log LOG = LogFactory.getLog(MCP3008Adc.class);

    private final Reading.Type readingType;
    private final String units;
    private final short channelCommand;
    private SpiDevice spiDevice;
    private boolean initiated;

    @Inject
    public MCP3008Adc(SpiChannel spiChannel, InputChannel inputChannel, Reading.Type readingType, String units) {
	LOG.info("Initializing analog sensor through MCP3008 at spiChannel '" + spiChannel + "', input channel '" + inputChannel + "', readingType '"
		+ readingType + "', units '" + units + "'");
	this.readingType = readingType;
	this.units = units;
	this.channelCommand = toCommand(inputChannel.channel);
	try {
	    this.spiDevice = new SpiDevice(spiChannel, inputChannel);
	    this.initiated = true;
	    LOG.info("Initializing of analog sensor through MCP3008 finished OK.");
	} catch (Exception e) {
	    LOG.error("Unable to initialize analog sensor reader through MCP3008.", e);
	    this.initiated = false;
	}
    }

    private short toCommand(short channel) {
	short command = (short) ((channel + 8) << 4);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("command: " + toBinary(command) + ". channel: " + toBinary(channel));
	}
	return command;
    }

    private int readAnalog() {
	// send 3 bytes command - "1", channel command and some extra byte 0
	// http://hertaville.com/2013/07/24/interfacing-an-spi-adc-mcp3008-chip-to-the-raspberry-pi-using-c/ or
	// http://raspberrypihobbyist.blogspot.cz/2012/12/analog-interface.html
	short[] data = new short[] { 1, (short) channelCommand, 0 };
	short[] result = spiDevice.readWrite(data);

	if (LOG.isDebugEnabled()) {
	    for (short s : data) {
		LOG.debug("Input for SPI: " + s + ". Binary: " + toBinary(s));
	    }
	    for (short s : result) {
		LOG.debug("Output from SPI: " + s + ". Binary: " + toBinary(s));
	    }
	}

	// now take 8 and 9 bit from second byte (& with 0b11 and shift) and the whole last byte to form the value
	int analogValue = (((int)data[1] & 3) << 8) + data[2];

	if (LOG.isDebugEnabled()) {
	    LOG.debug("Result: " + analogValue + ". In binary: " + toBinary(analogValue));
	}

	return analogValue;
    }

    private String toBinary(int number) {
	return String.format("%8s", Integer.toBinaryString(number)).replace(' ', '0');
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

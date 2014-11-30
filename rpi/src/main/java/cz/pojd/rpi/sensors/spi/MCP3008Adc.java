package cz.pojd.rpi.sensors.spi;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.spi.SpiChannel;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.gpio.Gpio;

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
    private boolean initiated;
    private GpioPinAnalogInput input;

    @Inject
    public MCP3008Adc(Gpio gpio, SpiChannel spiChannel, Pin inputPin, Reading.Type readingType, String units) {
	LOG.info("Initializing analog sensor through MCP3008 at spiChannel '" + spiChannel + "', input pin '" + inputPin + "', readingType '"
		+ readingType + "', units '" + units + "'");
	this.readingType = readingType;
	this.units = units;
	try {
	    this.input = gpio.provisionAnalogInputPin(new MCP3008GpioProvider(spiChannel), inputPin);
	    this.initiated = true;
	    LOG.info("Initializing of analog sensor through MCP3008 finished OK.");
	} catch (Exception e) {
	    LOG.error("Unable to initialize analog sensor reader through MCP3008.", e);
	    this.initiated = false;
	}
    }

    @Override
    public List<Reading> readAll() {
	return Collections.singletonList(read());
    }

    @Override
    public Reading read() {
	try {
	    if (isInitiated()) {
		return Reading.newBuilder().type(readingType).doubleValue(input.getValue()).units(units).build();
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

package cz.pojd.rpi.sensors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import cz.pojd.rpi.sensors.gpio.Gpio;
import cz.pojd.rpi.sensors.spi.MCP3008Adc;
import cz.pojd.rpi.sensors.spi.MCP3008Adc.CsChannel;
import cz.pojd.rpi.sensors.spi.MCP3008Adc.InputChannel;

public class MCP3008AdcTestCase {

    private MCP3008Adc mcpP3008Adc;

    @Mocked
    private Gpio gpio;
    @Mocked
    private GpioPinDigitalOutput output;
    @Mocked
    private GpioPinDigitalInput input;

    private CsChannel csChannel = CsChannel.CS0;
    private InputChannel channel = InputChannel.CH0;

    @Before
    public void setup() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalOutputPin(withInstanceOf(Pin.class), anyString, withInstanceOf(PinState.class));
		result = output;

		gpio.provisionDigitalInputPin(withInstanceOf(Pin.class), anyString);
		result = input;

		input.isHigh();
		returns(true, true, false, false, true, false, true, false, false, true, false, false);
	    }
	};

	mcpP3008Adc = new MCP3008Adc(gpio, csChannel, channel, Reading.Type.light, "");
    }

    @Test
    public void testReadAllReturnsOneEntry() {
	assertEquals(1, mcpP3008Adc.readAll().size());
    }

    @Test
    public void testReadReturnsInvalidIfReadFails() {
	new NonStrictExpectations() {
	    {
		input.isHigh();
		result = new RuntimeException("Some fake error");
	    }
	};
	Reading result;
	try {
	    result = mcpP3008Adc.read();
	} catch (Exception e) {
	    fail("No exception expected here, but got " + e);
	    e.printStackTrace();
	    return;
	}

	assertFalse(result.isValid());
    }

    @Test
    public void testReadReturnsValid() {
	Reading result = mcpP3008Adc.read();
	assertTrue(result.isValid());
	assertEquals(1618, result.getDoubleValue(), 0.001);
    }

    @Test
    public void testIsInitiatedReturnsFalseIfExceptionThrownDuringInit() {
	new NonStrictExpectations() {
	    {
		gpio.provisionDigitalOutputPin(withInstanceOf(Pin.class), anyString, withInstanceOf(PinState.class));
		result = new RuntimeException("Some fake error");
	    }
	};

	mcpP3008Adc = new MCP3008Adc(gpio, csChannel, channel, Reading.Type.light, "");
	assertFalse(mcpP3008Adc.isInitiated());
    }

}

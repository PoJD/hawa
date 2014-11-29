package cz.pojd.rpi.sensors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import cz.pojd.rpi.sensors.spi.MCP3008Adc;
import cz.pojd.rpi.sensors.spi.MCP3008Adc.InputChannel;

public class MCP3008AdcTestCase {

    private MCP3008Adc mcpP3008Adc;

    @Mocked
    private SpiDevice spiDevice;
    @Mocked
    private SpiFactory spiFactory;

    private SpiChannel spiChannel = SpiChannel.CS0;
    private InputChannel inputChannel = InputChannel.CH0;

    @Before
    public void setup() throws IOException {
	new NonStrictExpectations() {
	    {
		SpiFactory.getInstance(spiChannel);
		result = spiDevice;

		spiDevice.write(anyShort, anyShort, anyShort);
		result = new short[] { 0, 0b01, 0b11111111 };
	    }
	};

	mcpP3008Adc = new MCP3008Adc(spiChannel, inputChannel, Reading.Type.light, "");
    }

    @Test
    public void testReadAllReturnsOneEntry() {
	assertEquals(1, mcpP3008Adc.readAll().size());
    }

    @Test
    public void testReadReturnsInvalidIfReadFails() throws IOException {
	new NonStrictExpectations() {
	    {
		spiDevice.write(anyShort, anyShort, anyShort);
		result = new IOException("Some fake error");
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
	assertEquals(511, result.getDoubleValue(), 0.001);
    }

    @Test
    public void testIsInitiatedReturnsFalseIfExceptionThrownDuringInit() throws IOException {
	new NonStrictExpectations() {
	    {
		SpiFactory.getInstance(spiChannel);
		result = new IOException("Some fake error");
	    }
	};

	mcpP3008Adc = new MCP3008Adc(spiChannel, inputChannel, Reading.Type.light, "");
	assertFalse(mcpP3008Adc.isInitiated());
    }
}

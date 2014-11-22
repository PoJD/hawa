package cz.pojd.rpi.sensors.spi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.wiringpi.Spi;

/**
 * Generic SPI Device. Abstracts the access to SPI through PI4J.
 *
 * @author Lubos Housa
 * @since Nov 21, 2014 10:56:28 PM
 */
public class SpiDevice {
    private static final Log LOG = LogFactory.getLog(SpiDevice.class);
    public static final int SPI_SPEED = 1000000; // 1MHz (range is 500kHz - 32MHz)

    public enum SpiChannel {
	CS0(0), CS1(1);

	private final short channel;

	private SpiChannel(int channel) {
	    this.channel = (short) channel;
	}

	public short getChannel() {
	    return channel;
	}
    }

    public enum InputChannel {
	CH0(0), CH1(1), CH2(2), CH3(3), CH4(4), CH5(5), CH6(6), CH7(7);

	public final short channel;

	private InputChannel(int channel) {
	    this.channel = (short) channel;
	}

	public short getChannel() {
	    return channel;
	}
    }

    private final SpiChannel spiChannel;

    /**
     * Creates the SPI Device at the given spi and input channel
     * 
     * @param spiChannel
     *            spi channel to use
     */
    public SpiDevice(SpiChannel spiChannel) {
	LOG.info("Initializing Spi device at spiChannel " + spiChannel);
	this.spiChannel = spiChannel;
	try {
	    int fd = Spi.wiringPiSPISetup(spiChannel.getChannel(), SPI_SPEED);
	    if (fd <= -1) {
		throw new SpiAccessException("SPI port setup failed, wiringSPISetup returned " + fd);
	    }
	    LOG.info("Spi device initiated successfully. wiringPiSPISetup returned " + fd);
	} catch (UnsatisfiedLinkError e) {
	    throw new SpiAccessException("SPI port setup failed, no SPI available.", e);
	}
    }

    /**
     * Attempts to read/write data through this SPI device
     * 
     * @param data
     *            input of the operation
     * @return output of the operation from wiringPI
     */
    public short[] readWrite(short[] data) {
	short[] result = Spi.wiringPiSPIDataRW(spiChannel.getChannel(), data);
	if (result == null) {
	    throw new SpiAccessException("Some I/O error occured when talking to SPI. Spi.wiringPiSPIDataRW returned null.");
	}
	return result;
    }
}

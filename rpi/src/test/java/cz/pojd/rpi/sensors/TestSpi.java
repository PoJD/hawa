package cz.pojd.rpi.sensors;

import cz.pojd.rpi.sensors.spi.SpiDevice.InputChannel;

/**
 * Test class to confirm out the spi bit operations
 *
 * @author Lubos Housa
 * @since Nov 22, 2014 11:51:54 AM
 */
public class TestSpi {

    public static void main(String[] args) {
	short[] data = new short[] { 1,  toCommand(InputChannel.CH7.channel), 0 };
	for (int b : data) {
	    System.out.println("data: " + s(b, 8) + ". Int: " + b);
	}

	short b2 = 0b0;
	short b3 = 240;

	int result = ((b2 & 0b11) << 8) + b3;

	System.out.println("b2: " + s(b2, 8) + ". b3: " + s(b3, 8) + ". result:" + s(result, 8) + ". normal result: " + result);
    }

    public static String s(int number, int bits) {
	return String.format("%" + bits + "s", Integer.toBinaryString(number)).replace(' ', '0');
    }
    private static short toCommand(int channel) {
	return (short) ((channel + 8) << 4);
    }
}

package cz.pojd.rpi.sensors.spi;

public class SpiAccessException extends RuntimeException {

    private static final long serialVersionUID = 8990880755875850761L;

    public SpiAccessException(String message) {
	super(message);
    }

    public SpiAccessException(String message, Throwable cause) {
	super(message, cause);
    }
}

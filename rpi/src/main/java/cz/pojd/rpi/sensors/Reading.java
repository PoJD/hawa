package cz.pojd.rpi.sensors;

/**
 * Reading is a reading of a value from a Sensor
 * 
 * @author Lubos Housa
 * @since Aug 3, 2014 1:19:12 AM
 */
public class Reading {

    public enum Type {
	temperature, pressure
    }

    public static final class Builder {
	private Type type;
	private String value;

	private Builder() {
	}

	public Reading build() {
	    return new Reading(type, value);
	}

	public Builder type(Type type) {
	    this.type = type;
	    return this;
	}

	public Builder value(String value) {
	    this.value = value;
	    return this;
	}
    }

    public static final String UNKNOWN_VALUE = "0";

    public static final Builder newBuilder() {
	return new Builder();
    }

    public static final Reading unknown(Type type) {
	return new Reading(type, UNKNOWN_VALUE);
    }

    private final Type type;
    private final String value;

    private Reading(Type type, String value) {
	this.type = type;
	this.value = value;
    }

    public Type getType() {
	return type;
    }

    public String getValue() {
	return value;
    }

    @Override
    public String toString() {
	return "Reading [type=" + type + ", value=" + value + "]";
    }
}

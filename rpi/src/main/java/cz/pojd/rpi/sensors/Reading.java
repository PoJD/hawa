package cz.pojd.rpi.sensors;

/**
 * Reading is a reading of a value from a Sensor
 * 
 * @author Lubos Housa
 * @since Aug 3, 2014 1:19:12 AM
 */

public class Reading {

    public enum Type {
	temperatureB, temperatureD, pressure, humidity, generic
    }

    public static final class Builder {
	private Type type;
	private double doubleValue;
	private String units;

	private Builder() {
	}

	public Reading build() {
	    return new Reading(type, doubleValue, double2String(doubleValue) + (units != null ? units : ""), true);
	}

	public Builder type(Type type) {
	    this.type = type;
	    return this;
	}

	public Builder doubleValue(double doubleValue) {
	    this.doubleValue = doubleValue;
	    return this;
	}

	public Builder units(String units) {
	    this.units = units;
	    return this;
	}

	private String double2String(double d) {
	    return String.format("%.2f", d);
	}
    }

    public static final Builder newBuilder() {
	return new Builder();
    }

    public static final Reading invalid(Type type) {
	return new Reading(type, 0, "0", false);
    }

    /*
     * private constructor added for Jackson to be able to create instance from JSON. Fields not final for the same reason.
     */

    private Type type;
    private double doubleValue;
    private String stringValue;
    private boolean valid;

    private Reading() {
    }

    private Reading(Type type, double doubleValue, String stringValue, boolean valid) {
	this.type = type;
	this.doubleValue = doubleValue;
	this.stringValue = stringValue;
	this.valid = valid;
    }

    public Type getType() {
	return type;
    }

    public double getDoubleValue() {
	return doubleValue;
    }

    public String getStringValue() {
	return stringValue;
    }

    public boolean isValid() {
	return valid;
    }

    @Override
    public String toString() {
	return "Reading [type=" + type + ", doubleValue=" + doubleValue + ", stringValue=" + stringValue + ", valid=" + valid + "]";
    }
}

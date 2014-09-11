package cz.pojd.rpi.sensors;

/**
 * Simple wrapper for a sensor state
 *
 * @author Lubos Housa
 * @since Sep 10, 2014 11:39:04 PM
 */
public class SensorState {

    private boolean enabled;
    private boolean on;

    public SensorState() {
    }

    public SensorState(Builder builder) {
	this.enabled = builder.enabled;
	this.on = builder.on;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public boolean isOn() {
	return on;
    }

    @Override
    public String toString() {
	return "SensorState [enabled=" + enabled + ", on=" + on + "]";
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    public static class Builder {
	private boolean enabled;
	private boolean on;

	private Builder() {
	}

	public Builder enabled(boolean enabled) {
	    this.enabled = enabled;
	    return this;
	}

	public Builder on(boolean on) {
	    this.on = on;
	    return this;
	}

	public SensorState build() {
	    return new SensorState(this);
	}
    }
}

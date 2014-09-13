package cz.pojd.rpi;

/**
 * Simple wrapper for a sensor/control state
 *
 * @author Lubos Housa
 * @since Sep 10, 2014 11:39:04 PM
 */
public class State {

    private boolean initiated;
    private boolean enabled;
    private boolean switchedOn;
    private boolean switchable;

    public State() {
    }

    public State(Builder builder) {
	this.initiated = builder.initiated;
	this.enabled = builder.enabled;
	this.switchedOn = builder.switchedOn;
	this.switchable = builder.switchable;
    }

    public boolean isInitiated() {
	return initiated;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public boolean isSwitchedOn() {
	return switchedOn;
    }

    public boolean isSwitchable() {
	return switchable;
    }

    @Override
    public String toString() {
	return "State [initiated=" + initiated + ", enabled=" + enabled + ", switchedOn=" + switchedOn + ", switchable=" + switchable + "]";
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    public static class Builder {
	private boolean initiated;
	private boolean enabled;
	private boolean switchedOn;
	private boolean switchable;

	private Builder() {
	}

	public Builder initiated(boolean initiated) {
	    this.initiated = initiated;
	    return this;
	}

	public Builder enabled(boolean enabled) {
	    this.enabled = enabled;
	    return this;
	}

	public Builder switchedOn(boolean switchedOn) {
	    this.switchedOn = switchedOn;
	    return this;
	}

	public Builder switchable(boolean switchable) {
	    this.switchable = switchable;
	    return this;
	}

	public State build() {
	    return new State(this);
	}
    }
}

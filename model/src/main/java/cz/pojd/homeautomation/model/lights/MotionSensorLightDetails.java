package cz.pojd.homeautomation.model.lights;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;

/**
 * Simple POJO for motion sensor details using the builder pattern
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 1:06:03 AM
 */
public class MotionSensorLightDetails {

    private final LightCapable lightCapable;
    private final GpioProvider switchProvider, controlProvider;
    private final Pin motionSensorPin, lightSwitchPin, lightControlPin;
    private final Integer lightLevelSensorAddress;
    private final Double lightLevelTreshold;

    private MotionSensorLightDetails(Builder builder) {
	this.lightCapable = builder.lightCapable;
	this.switchProvider = builder.switchProvider;
	this.controlProvider = builder.controlProvider;
	this.motionSensorPin = builder.motionSensorPin;
	this.lightSwitchPin = builder.lightSwitchPin;
	this.lightControlPin = builder.lightControlPin;
	this.lightLevelSensorAddress = builder.lightLevelSensorAddress;
	this.lightLevelTreshold = builder.lightLevelTreshold;
    }

    public LightCapable getLightCapable() {
	return lightCapable;
    }

    public GpioProvider getSwitchProvider() {
	return switchProvider;
    }

    public GpioProvider getControlProvider() {
	return controlProvider;
    }

    public Pin getMotionSensorPin() {
	return motionSensorPin;
    }

    public Pin getLightSwitchPin() {
	return lightSwitchPin;
    }

    public Pin getLightControlPin() {
	return lightControlPin;
    }

    public Integer getLightLevelSensorAddress() {
	return lightLevelSensorAddress;
    }

    public Double getLightLevelTreshold() {
	return lightLevelTreshold;
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    public static class Builder {
	private LightCapable lightCapable;
	private GpioProvider switchProvider, controlProvider;
	private Pin motionSensorPin, lightSwitchPin, lightControlPin;
	private Integer lightLevelSensorAddress;
	private Double lightLevelTreshold;

	private Builder() {
	}

	public Builder lightCapable(LightCapable lightCapable) {
	    this.lightCapable = lightCapable;
	    return this;
	}

	public Builder switchProvider(GpioProvider switchProvider) {
	    this.switchProvider = switchProvider;
	    return this;
	}

	public Builder controlProvider(GpioProvider controlProvider) {
	    this.controlProvider = controlProvider;
	    return this;
	}

	public Builder motionSensorPin(Pin motionSensorPin) {
	    this.motionSensorPin = motionSensorPin;
	    return this;
	}

	public Builder lightSwitchPin(Pin lightSwitchPin) {
	    this.lightSwitchPin = lightSwitchPin;
	    return this;
	}

	public Builder lightControlPin(Pin lightControlPin) {
	    this.lightControlPin = lightControlPin;
	    return this;
	}

	public Builder lightLevelSensorAddress(Integer lightLevelSensorAddress) {
	    this.lightLevelSensorAddress = lightLevelSensorAddress;
	    return this;
	}

	public Builder lightLevelTreshold(Double lightLevelTreshold) {
	    this.lightLevelTreshold = lightLevelTreshold;
	    return this;
	}

	public MotionSensorLightDetails build() {
	    return new MotionSensorLightDetails(this);
	}
    }
}

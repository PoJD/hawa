package cz.pojd.homeautomation.hawa.rooms;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

/**
 * Specification of a room. Used to detect information about a room at runtime
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 10:28:48 PM
 */
public class RoomSpecification {

    private final String name;
    private final String temperatureID;
    private final ObservableSensor autolights;
    private final Control lightControl;
    private final Floor floor;

    private RoomSpecification(Builder builder) {
	this.name = builder.name;
	this.temperatureID = builder.temperatureID;
	this.autolights = builder.autolights;
	this.lightControl = builder.lightControl;
	this.floor = builder.floor != null ? builder.floor : Floor.BASEMENT;
    }

    public String getName() {
	return name;
    }

    public String getTemperatureID() {
	return temperatureID;
    }

    public ObservableSensor getAutolights() {
	return autolights;
    }

    public Control getLightControl() {
	return lightControl;
    }

    public Floor getFloor() {
	return floor;
    }

    @Override
    public String toString() {
	return "RoomSpecification [name=" + name + ", temperatureID=" + temperatureID + ", autolights=" + autolights + ", lightControl="
		+ lightControl + ", floor=" + floor + "]";
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    public static class Builder {
	private String name;
	private String temperatureID;
	private ObservableSensor autolights;
	private Control lightControl;
	private Floor floor;

	public Builder name(String name) {
	    this.name = name;
	    return this;
	}

	public Builder temperatureID(String temperatureID) {
	    this.temperatureID = temperatureID;
	    return this;
	}

	public Builder autolights(ObservableSensor autolights) {
	    this.autolights = autolights;
	    return this;
	}

	public Builder lightControl(Control lightControl) {
	    this.lightControl = lightControl;
	    return this;
	}

	public Builder floor(Floor floor) {
	    this.floor = floor;
	    return this;
	}

	public RoomSpecification build() {
	    return new RoomSpecification(this);
	}
    }
}

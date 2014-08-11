package cz.pojd.homeautomation.hawa.rooms;

/**
 * Specification of a room. Used to detect information about a room at runtime
 * 
 * @author Lubos Housa
 * @since Aug 1, 2014 10:28:48 PM
 */
public class RoomSpecification {

    private final String name;
    private final String temperatureID;
    private final Boolean autolights;
    private final Floor floor;

    private RoomSpecification(Builder builder) {
	this.name = builder.name;
	this.temperatureID = builder.temperatureID;
	this.autolights = builder.autolights;
	this.floor = builder.floor != null ? builder.floor : Floor.BASEMENT;
    }

    public String getName() {
	return name;
    }

    public String getTemperatureID() {
	return temperatureID;
    }

    public Boolean getAutolights() {
	return autolights;
    }

    public Floor getFloor() {
	return floor;
    }

    @Override
    public String toString() {
	return "RoomSpecification [name=" + name + ", temperatureID=" + temperatureID + ", autolights=" + autolights + ", floor=" + floor + "]";
    }

    public static Builder newBuilder() {
	return new Builder();
    }

    public static class Builder {
	private String name;
	private String temperatureID;
	private Boolean autolights;
	private Floor floor;

	public Builder name(String name) {
	    this.name = name;
	    return this;
	}

	public Builder temperatureID(String temperatureID) {
	    this.temperatureID = temperatureID;
	    return this;
	}

	public Builder autolights(Boolean autolights) {
	    this.autolights = autolights;
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

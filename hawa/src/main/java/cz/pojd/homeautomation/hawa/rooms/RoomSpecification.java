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

    public RoomSpecification(String name, String temperatureID) {
	this.name = name;
	this.temperatureID = temperatureID;
    }

    public String getName() {
	return name;
    }

    public String getTemperatureID() {
	return temperatureID;
    }

    @Override
    public String toString() {
	return "RoomSpecification [name=" + name + ", temperatureID=" + temperatureID + "]";
    }
}

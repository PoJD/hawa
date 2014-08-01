package cz.pojd.homeautomation.hawa.rooms;


/**
 * Simple POJO to hold information about a room
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
public class RoomState {

    private String name;
    private Double temperature;
    private boolean autoLights;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Double getTemperature() {
	return temperature;
    }

    public void setTemperature(Double temperature) {
	this.temperature = temperature;
    }

    public boolean isAutoLights() {
	return autoLights;
    }

    public void setAutoLights(boolean autoLights) {
	this.autoLights = autoLights;
    }

    @Override
    public String toString() {
	return "RoomState [name=" + name + ", temperature=" + temperature + ", autoLights=" + autoLights + "]";
    }
}

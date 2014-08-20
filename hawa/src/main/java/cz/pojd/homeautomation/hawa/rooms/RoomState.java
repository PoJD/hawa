package cz.pojd.homeautomation.hawa.rooms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple POJO to hold information about a room
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomState {

    private String name;
    private String temperature;
    private Boolean autoLights;
    private Floor floor;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getTemperature() {
	return temperature;
    }

    public void setTemperature(String temperature) {
	this.temperature = temperature;
    }

    public Boolean getAutoLights() {
	return autoLights;
    }

    public void setAutoLights(Boolean autoLights) {
	this.autoLights = autoLights;
    }

    public Floor getFloor() {
	return floor;
    }

    public void setFloor(Floor floor) {
	this.floor = floor;
    }

    @Override
    public String toString() {
	return "RoomState [name=" + name + ", temperature=" + temperature + ", autoLights=" + autoLights + ", floor=" + floor + "]";
    }
}

package cz.pojd.homeautomation.hawa.rooms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.rpi.sensors.Sensor;

/**
 * A room is a runtime object representing a room.
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {

    private String name;
    private Sensor temperatureSensor;
    private boolean autoLights = true;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Sensor getTemperatureSensor() {
	return temperatureSensor;
    }

    public void setTemperatureSensor(Sensor temperatureSensor) {
	this.temperatureSensor = temperatureSensor;
    }

    public boolean isAutoLights() {
	return autoLights;
    }

    public void setAutoLights(boolean autoLights) {
	this.autoLights = autoLights;
    }

    @Override
    public String toString() {
	return "Room [name=" + name + ", temperatureSensor=" + temperatureSensor + ", autoLights=" + autoLights + "]";
    }
}

package cz.pojd.homeautomation.model.rooms;

import cz.pojd.homeautomation.model.lights.LightCapable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * A room is a runtime object representing a room.
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
public class Room extends LightCapable {

    private String name;
    private Sensor temperatureSensor;
    private Floor floor;

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

    public Floor getFloor() {
	return floor;
    }

    public void setFloor(Floor floor) {
	this.floor = floor;
    }

    public RoomDetail getLastDetail() {
	return (RoomDetail) super.getLastDetail();
    }

    @Override
    public String toString() {
	return "Room [getName()=" + getName() + ", getTemperatureSensor()=" + getTemperatureSensor() + ", getFloor()=" + getFloor()
		+ ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()=" + getLightSwitch() + ", getLightControl()=" + getLightControl()
		+ ", getLightLevelSensor()=" + getLightLevelSensor() + "]";
    }
}

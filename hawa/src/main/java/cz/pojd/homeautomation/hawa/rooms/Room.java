package cz.pojd.homeautomation.hawa.rooms;

import cz.pojd.rpi.sensors.Sensor;

/**
 * A room is a runtime object representing a room.
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
public class Room {

    private String name;
    private Sensor temperatureSensor;
    private Boolean autoLights;
    private Floor floor;
    private RoomDetail roomDetail;

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

    public Boolean getAutoLights() {
	return autoLights;
    }

    public void setAutoLights(Boolean autoLights) {
	this.autoLights = autoLights;
	if (getRoomDetail() != null) {
	    // change the actual room state too
	    getRoomDetail().setAutoLights(autoLights);
	}
    }

    public Floor getFloor() {
	return floor;
    }

    public void setFloor(Floor floor) {
	this.floor = floor;
    }

    public RoomDetail getRoomDetail() {
	return roomDetail;
    }

    public void setRoomDetail(RoomDetail roomDetail) {
	this.roomDetail = roomDetail;
    }

    @Override
    public String toString() {
	return "Room [name=" + name + ", temperatureSensor=" + temperatureSensor + ", autoLights=" + autoLights + ", floor=" + floor + "]";
    }
}

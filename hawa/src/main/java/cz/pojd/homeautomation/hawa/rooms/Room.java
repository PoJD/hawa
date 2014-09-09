package cz.pojd.homeautomation.hawa.rooms;

import cz.pojd.homeautomation.hawa.AutolightsCapable;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Sensor;

/**
 * A room is a runtime object representing a room.
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
public class Room extends AutolightsCapable {

    private String name;
    private Sensor temperatureSensor;
    private Control lightControl;
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


    public Control getLightControl() {
	return lightControl;
    }

    public void setLightControl(Control lightControl) {
	this.lightControl = lightControl;
    }

    public Floor getFloor() {
	return floor;
    }

    public void setFloor(Floor floor) {
	this.floor = floor;
    }

    @Override
    public String toString() {
	return "Room [name=" + name + ", temperatureSensor=" + temperatureSensor + ", autoLights=" + getAutoLights() + ", lightControl=" + lightControl
		+ ", floor=" + floor + "]";
    }

}

package cz.pojd.homeautomation.hawa.rooms;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.hawa.graphs.GraphData;
import cz.pojd.homeautomation.hawa.lights.LightCapableDetail;
import cz.pojd.rpi.sensors.Reading;

/**
 * Simple POJO to hold information about a room
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDetail extends LightCapableDetail {

    private String name;
    private Floor floor;
    private String lastUpdate;
    private Reading temperature;
    private GraphData[] temperatureHistory;

    public RoomDetail() {
    }

    public RoomDetail(RoomDetail copy) {
	super(copy);
	setName(copy.getName());
	setTemperature(copy.getTemperature());
	setFloor(copy.getFloor());
	setLastUpdate(copy.getLastUpdate());
	setTemperatureHistory(copy.getTemperatureHistory());
    }

    public RoomDetail(Room room) {
	super(room);
	setName(room.getName());
	setTemperature(room.getTemperatureSensor().read());
	setFloor(room.getFloor());
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Floor getFloor() {
	return floor;
    }

    public void setFloor(Floor floor) {
	this.floor = floor;
    }

    public String getLastUpdate() {
	return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
	this.lastUpdate = lastUpdate;
    }

    public Reading getTemperature() {
	return temperature;
    }

    public void setTemperature(Reading temperature) {
	this.temperature = temperature;
    }

    public boolean isValidTemperature() {
	return getTemperature() != null && getTemperature().isValid();
    }

    public GraphData[] getTemperatureHistory() {
	return temperatureHistory;
    }

    public void setTemperatureHistory(GraphData[] temperatureHistory) {
	this.temperatureHistory = temperatureHistory;
    }

    @Override
    public String toString() {
	return "RoomDetail [getName()=" + getName() + ", getFloor()=" + getFloor() + ", getLastUpdate()=" + getLastUpdate() + ", getTemperature()="
		+ getTemperature() + ", isValidTemperature()=" + isValidTemperature() + ", getTemperatureHistory()="
		+ Arrays.toString(getTemperatureHistory()) + ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()=" + getLightSwitch()
		+ ", getLightControl()=" + getLightControl() + "]";
    }
}

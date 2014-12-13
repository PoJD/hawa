package cz.pojd.homeautomation.model.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.refresh.RefreshedLightCapableDetail;
import cz.pojd.homeautomation.model.spring.RoomSpecification;
import cz.pojd.homeautomation.model.web.GraphData;
import cz.pojd.rpi.sensors.Reading;

/**
 * Simple POJO to hold information about a room
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDetail extends RefreshedLightCapableDetail {

    private RoomSpecification specification;
    private String name;
    private Floor floor;
    private Reading temperature;
    private GraphData[] temperatureHistory;
    private EntryDetail[] entries;

    public RoomDetail() {
    }

    public RoomDetail(RoomDetail copy) {
	super(copy);
	setSpecification(copy.getSpecification());
	setName(copy.getName());
	setFloor(copy.getFloor());
	setTemperature(copy.getTemperature());
	setTemperatureHistory(copy.getTemperatureHistory());
	setEntries(copy.getEntries());
    }

    public RoomDetail(Room room) {
	this(room, true);
    }

    public RoomDetail(Room room, boolean readTemperature) {
	super(room);
	setSpecification(room.getSpecification());
	setName(room.getName());
	setFloor(room.getFloor());

	if (room.getEntries() != null) {
	    List<EntryDetail> entries = new ArrayList<>();
	    for (Entry entry : room.getEntries()) {
		entries.add(new EntryDetail(entry));
	    }
	    setEntries(entries.toArray(new EntryDetail[entries.size()]));
	}

	if (readTemperature) {
	    setTemperature(room.getTemperatureSensor().read());
	}
    }

    public RoomSpecification getSpecification() {
	return specification;
    }

    public void setSpecification(RoomSpecification specification) {
	this.specification = specification;
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

    public EntryDetail[] getEntries() {
	return entries;
    }

    public void setEntries(EntryDetail[] entries) {
	this.entries = entries;
    }

    @Override
    public String toString() {
	return "RoomDetail [getSpecification()=" + getSpecification() + ", getName()=" + getName() + ", getFloor()=" + getFloor()
		+ ", getTemperature()=" + getTemperature() + ", isValidTemperature()=" + isValidTemperature() + ", getTemperatureHistory()="
		+ Arrays.toString(getTemperatureHistory()) + ", getEntries()=" + Arrays.toString(getEntries()) + ", getLastUpdate()="
		+ getLastUpdate() + ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()=" + getLightSwitch() + ", getLightControl()="
		+ getLightControl() + ", getLightLevel()=" + getLightLevel() + "]";
    }
}

package cz.pojd.homeautomation.model.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.Pin;

import cz.pojd.homeautomation.model.Floor;

/**
 * Room specification holds all information to create the rooms
 *
 * @author Lubos Housa
 * @since Sep 10, 2014 9:58:42 PM
 */
public enum RoomSpecification {
    HALL_DOWN("Hall down", "28-0000060a84d1", MCP23017Pin.GPIO_A0, MCP23017Pin.GPIO_A1, null, MCP23017Pin.GPIO_A2, 50),
    KITCHEN("Kitchen", "28-0000060a84d1", MCP23017Pin.GPIO_A3, EntrySpecification.KITCHEN_WEST),
    LIVING_ROOM("Living room", "28-0000060a84d1", MCP23017Pin.GPIO_A4),
    BATHROOM_DOWN("Bathroom down", "28-0000060a84d1", MCP23017Pin.GPIO_A5),
    WC_DOWN("WC down", "28-0000060a84d1", MCP23017Pin.GPIO_A6),
    DOWN_ROOM("Down room", "28-0000060a84d1", MCP23017Pin.GPIO_A7),
    HALL_UP("Hall up", "28-0000060a84d1", MCP23017Pin.GPIO_A0, MCP23017Pin.GPIO_A1, Floor.First, MCP23017Pin.GPIO_A2, 50),
    BEDROOM("Bedroom", "28-0000060a84d1", MCP23017Pin.GPIO_A3, Floor.First),
    CHILD_ROOM_1("Child room 1", "28-0000060a84d1", MCP23017Pin.GPIO_A4, Floor.First),
    CHILD_ROOM_2("Child room 2", "28-0000060a84d1", MCP23017Pin.GPIO_A5, Floor.First),
    CHILD_ROOM_3("Child room 3", "28-0000060a84d1", MCP23017Pin.GPIO_A6, Floor.First),
    CHILD_ROOM_4("Child room 4", "28-0000060a84d1", MCP23017Pin.GPIO_A7, Floor.First),
    BATHROOM_BEDROOM("Bathroom bed", "28-0000060a84d1", MCP23017Pin.GPIO_B0, Floor.First),
    WC_BEDROOM("WC bed", "28-0000060a84d1", MCP23017Pin.GPIO_B1, Floor.First),
    BATHROOM_UP("Bathroom up", "28-0000060a84d1", MCP23017Pin.GPIO_B2, Floor.First),
    WC_UP("WC up", "28-0000060a84d1", MCP23017Pin.GPIO_B3, Floor.First),
    LAUNDRY_ROOM("Laundry room", "28-0000060a84d1", MCP23017Pin.GPIO_B4, Floor.First);

    private final String name;
    private final String temperatureID;
    private final Pin motionSensorPin, lightPin, lightLevelSensorPin;
    private final Floor floor;
    private final double lightLevelTreshold;
    private final List<EntrySpecification> entrySpecifications;

    private RoomSpecification(String name, String temperatureID, Pin lightPin, EntrySpecification... entrySpecifications) {
	this(name, temperatureID, lightPin, null, null, null, 0, entrySpecifications);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Pin motionSensorPin, EntrySpecification... entrySpecifications) {
	this(name, temperatureID, lightPin, motionSensorPin, null, null, 0, entrySpecifications);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Floor floor, EntrySpecification... entrySpecifications) {
	this(name, temperatureID, lightPin, null, floor, null, 0, entrySpecifications);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Pin motionSensorPin, Floor floor, Pin lightLevelSensorPin,
	    double lightLevelTreshold, EntrySpecification... entrySpecifications) {
	this.name = name;
	this.temperatureID = temperatureID;
	this.lightPin = lightPin;
	this.motionSensorPin = motionSensorPin;
	this.floor = floor != null ? floor : Floor.Basement;
	this.lightLevelSensorPin = lightLevelSensorPin;
	this.lightLevelTreshold = lightLevelTreshold;
	if (entrySpecifications != null) {
	    this.entrySpecifications = Arrays.asList(entrySpecifications);
	} else {
	    this.entrySpecifications = new ArrayList<>();
	}
    }

    public String getId() {
	return name();
    }

    public String getName() {
	return name;
    }

    public Floor getFloor() {
	return floor;
    }

    public String getTemperatureID() {
	return temperatureID;
    }

    public Pin getMotionSensorPin() {
	return motionSensorPin;
    }

    public Pin getLightPin() {
	return lightPin;
    }

    public Pin getLightLevelSensorPin() {
	return lightLevelSensorPin;
    }

    public double getLightLevelTreshold() {
	return lightLevelTreshold;
    }

    public List<EntrySpecification> getEntrySpecifications() {
	return entrySpecifications;
    }
}

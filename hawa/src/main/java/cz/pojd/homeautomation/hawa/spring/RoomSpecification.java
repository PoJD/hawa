package cz.pojd.homeautomation.hawa.spring;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.homeautomation.hawa.rooms.Floor;

/**
 * Room specification holds all information to create the rooms
 *
 * @author Lubos Housa
 * @since Sep 10, 2014 9:58:42 PM
 */
public enum RoomSpecification {
    HALL_DOWN("Hall down", "28-0000060a84d1", RaspiPin.GPIO_01, RaspiPin.GPIO_16), 
    KITCHEN("Kitchen", "28-0000060a84d1", RaspiPin.GPIO_02), 
    LIVING_ROOM("Living room", "28-0000060a84d1", RaspiPin.GPIO_03), 
    BATHROOM_DOWN("Bathroom down", "28-0000060a84d1", RaspiPin.GPIO_04), 
    WC_DOWN("WC down", "28-0000060a84d1", RaspiPin.GPIO_05), 
    DOWN_ROOM("Down room", "28-0000060a84d1", RaspiPin.GPIO_06), 
    HALL_UP("Hall up", "28-0000060a84d1", RaspiPin.GPIO_01, RaspiPin.GPIO_16, Floor.FIRST), 
    BEDROOM("Bedroom", "28-0000060a84d1", RaspiPin.GPIO_02, Floor.FIRST), 
    CHILD_ROOM_1("Child room 1", "28-0000060a84d1", RaspiPin.GPIO_03, Floor.FIRST), 
    CHILD_ROOM_2("Child room 2", "28-0000060a84d1", RaspiPin.GPIO_04, Floor.FIRST), 
    CHILD_ROOM_3("Child room 3", "28-0000060a84d1", RaspiPin.GPIO_05, Floor.FIRST), 
    CHILD_ROOM_4("Child room 4", "28-0000060a84d1", RaspiPin.GPIO_06, Floor.FIRST), 
    BATHROOM_BEDROOM("Bathroom bed", "28-0000060a84d1", RaspiPin.GPIO_07, Floor.FIRST), 
    WC_BEDROOM("WC bed", "28-0000060a84d1", RaspiPin.GPIO_08, Floor.FIRST), 
    BATHROOM_UP("Bathroom up", "28-0000060a84d1", RaspiPin.GPIO_09, Floor.FIRST), 
    WC_UP("WC up", "28-0000060a84d1", RaspiPin.GPIO_10, Floor.FIRST), 
    LAUNDRY_ROOM("Laundry room", "28-0000060a84d1", RaspiPin.GPIO_11, Floor.FIRST);

    private final String name;
    private final String temperatureID;
    private final Pin motionSensorPin, lightPin;
    private final Floor floor;

    private RoomSpecification(String name, String temperatureID, Pin lightPin) {
	this(name, temperatureID, lightPin, null, null);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Pin motionSensorPin) {
	this(name, temperatureID, lightPin, motionSensorPin, null);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Floor floor) {
	this(name, temperatureID, lightPin, null, floor);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Pin motionSensorPin, Floor floor) {
	this.name = name;
	this.temperatureID = temperatureID;
	this.lightPin = lightPin;
	this.motionSensorPin = motionSensorPin;
	this.floor = floor;
    }

    public String getName() {
	return name;
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

    public Floor getFloor() {
	return floor;
    }

    @Override
    public String toString() {
	return "RoomSpecification [name=" + name + ", temperatureID=" + temperatureID + ", motionSensorPin=" + motionSensorPin + ", lightPin=" + lightPin
		+ ", floor=" + floor + "]";
    }
}

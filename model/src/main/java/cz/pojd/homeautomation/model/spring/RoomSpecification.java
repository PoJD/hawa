package cz.pojd.homeautomation.model.spring;

import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.Pin;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.Source;
import cz.pojd.rpi.sensors.i2c.TSL2561LightSensor;

/**
 * Room specification holds all information to create the rooms
 *
 * @author Lubos Housa
 * @since Sep 10, 2014 9:58:42 PM
 */
public enum RoomSpecification implements Source {
    HALL_DOWN("Hall down", "28-0000060a84d1", MCP23017Pin.GPIO_A0, MCP23017Pin.GPIO_A1, null, TSL2561LightSensor.TSL2561_ADDRESS_LOW, 50),
    KITCHEN("Kitchen", "28-0000060a84d1", MCP23017Pin.GPIO_A2),
    LIVING_ROOM("Living room", "28-0000060a84d1", MCP23017Pin.GPIO_A3),
    BATHROOM_DOWN("Bathroom down", "28-0000060a84d1", MCP23017Pin.GPIO_A4),
    WC_DOWN("WC down", "28-0000060a84d1", MCP23017Pin.GPIO_A5),
    DOWN_ROOM("Down room", "28-0000060a84d1", MCP23017Pin.GPIO_A6),
    HALL_UP("Hall up", "28-0000060a84d1", MCP23017Pin.GPIO_A0, MCP23017Pin.GPIO_A1, Floor.First, TSL2561LightSensor.TSL2561_ADDRESS_HIGH, 50),
    BEDROOM("Bedroom", "28-0000060a84d1", MCP23017Pin.GPIO_A2, Floor.First),
    CHILD_ROOM_1("Child room 1", "28-0000060a84d1", MCP23017Pin.GPIO_A3, Floor.First),
    CHILD_ROOM_2("Child room 2", "28-0000060a84d1", MCP23017Pin.GPIO_A4, Floor.First),
    CHILD_ROOM_3("Child room 3", "28-0000060a84d1", MCP23017Pin.GPIO_A5, Floor.First),
    CHILD_ROOM_4("Child room 4", "28-0000060a84d1", MCP23017Pin.GPIO_A6, Floor.First),
    BATHROOM_BEDROOM("Bathroom bed", "28-0000060a84d1", MCP23017Pin.GPIO_A7, Floor.First),
    WC_BEDROOM("WC bed", "28-0000060a84d1", MCP23017Pin.GPIO_B1, Floor.First),
    BATHROOM_UP("Bathroom up", "28-0000060a84d1", MCP23017Pin.GPIO_B2, Floor.First),
    WC_UP("WC up", "28-0000060a84d1", MCP23017Pin.GPIO_B3, Floor.First),
    LAUNDRY_ROOM("Laundry room", "28-0000060a84d1", MCP23017Pin.GPIO_B4, Floor.First);

    private final String name;
    private final String temperatureID;
    private final Pin motionSensorPin, lightPin;
    private final Floor floor;
    private final int lightLevelSensorAddress;
    private final double lightLevelTreshold;

    private RoomSpecification(String name, String temperatureID, Pin lightPin) {
	this(name, temperatureID, lightPin, null, null, 0, 0);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Pin motionSensorPin) {
	this(name, temperatureID, lightPin, motionSensorPin, null, 0, 0);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Floor floor) {
	this(name, temperatureID, lightPin, null, floor, 0, 0);
    }

    private RoomSpecification(String name, String temperatureID, Pin lightPin, Pin motionSensorPin, Floor floor, int lightLevelSensorAddress,
	    double lightLevelTreshold) {
	this.name = name;
	this.temperatureID = temperatureID;
	this.lightPin = lightPin;
	this.motionSensorPin = motionSensorPin;
	this.floor = floor != null ? floor : Floor.Basement;
	this.lightLevelSensorAddress = lightLevelSensorAddress;
	this.lightLevelTreshold = lightLevelTreshold;
    }

    @Override
    public String getId() {
	return name();
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public Floor getFloor() {
	return floor;
    }

    @Override
    public boolean isOutdoor() {
	return false;
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

    public int getLightLevelSensorAddress() {
	return lightLevelSensorAddress;
    }

    public double getLightLevelTreshold() {
	return lightLevelTreshold;
    }
}

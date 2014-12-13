package cz.pojd.homeautomation.model.spring;

import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.Pin;

/**
 * Represents a window specification
 *
 * @author Lubos Housa
 * @since Nov 8, 2014 11:31:17 PM
 */
public enum EntrySpecification {
    MAIN_DOOR("Main door", MCP23017Pin.GPIO_A0),
    GARAGE_DOOR("Garage door", MCP23017Pin.GPIO_A1),
    KITCHEN_WEST("West kitchen window", MCP23017Pin.GPIO_A2);

    private final String name;
    private final Pin reedSwitchPin;

    private EntrySpecification(String name, Pin reedSwitchPin) {
	this.name = name;
	this.reedSwitchPin = reedSwitchPin;
    }

    public String getId() {
	return name();
    }

    public String getName() {
	return name;
    }

    public Pin getReedSwitchPin() {
	return reedSwitchPin;
    }
}

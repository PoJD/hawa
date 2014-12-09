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
    KITCHEN_WEST("West kitchen window", MCP23017Pin.GPIO_A0),
    MAIN_DOOR("Main door", MCP23017Pin.GPIO_A0, true);

    private final String name;
    private final Pin reedSwitchPin;
    private final boolean door;

    private EntrySpecification(String name, Pin reedSwitchPin) {
	this(name, reedSwitchPin, false);
    }

    private EntrySpecification(String name, Pin reedSwitchPin, boolean door) {
	this.name = name;
	this.reedSwitchPin = reedSwitchPin;
	this.door = door;
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

    public boolean isDoor() {
	return door;
    }
}

package cz.pojd.homeautomation.hawa.rooms;

import java.math.BigDecimal;

/**
 * Simple POJO to hold information about a room
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 12:51:36 AM
 */
public class Room {

    private String name;
    private BigDecimal temperature;
    private boolean autoLights;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public BigDecimal getTemperature() {
	return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
	this.temperature = temperature;
    }

    public boolean isAutoLights() {
	return autoLights;
    }

    public void setAutoLights(boolean autoLights) {
	this.autoLights = autoLights;
    }

    @Override
    public String toString() {
	return "Room [name=" + name + ", temperature=" + temperature + ", autoLights=" + autoLights + "]";
    }
}

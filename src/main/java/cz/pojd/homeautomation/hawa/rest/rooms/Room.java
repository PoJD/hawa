package cz.pojd.homeautomation.hawa.rest.rooms;

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

    @Override
    public String toString() {
	return "Room [name=" + name + ", temperature=" + temperature + "]";
    }
}

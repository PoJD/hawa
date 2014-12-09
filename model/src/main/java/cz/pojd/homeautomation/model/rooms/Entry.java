package cz.pojd.homeautomation.model.rooms;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.Source;
import cz.pojd.homeautomation.model.spring.EntrySpecification;
import cz.pojd.rpi.sensors.observable.GpioSwitch;

/**
 * Entry entity. Represents either a window or door.
 *
 * @author Lubos Housa
 * @since Dec 9, 2014 11:26:22 PM
 */
public class Entry implements Source {

    private EntrySpecification specification;
    private String name;
    private Room room;
    private GpioSwitch reedSwitch;

    public EntrySpecification getSpecification() {
	return specification;
    }

    public void setSpecification(EntrySpecification specification) {
	this.specification = specification;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Room getRoom() {
	return room;
    }

    public void setRoom(Room room) {
	this.room = room;
    }

    public GpioSwitch getReedSwitch() {
	return reedSwitch;
    }

    public void setReedSwitch(GpioSwitch reedSwitch) {
	this.reedSwitch = reedSwitch;
    }

    @Override
    public String getId() {
	return specification.getId();
    }

    @Override
    public Floor getFloor() {
	return room.getFloor();
    }

    @Override
    public boolean isOutdoor() {
	return false;
    }

    @Override
    public String toString() {
	return "Entry [getSpecification()=" + getSpecification() + ", getName()=" + getName() + ", getRoom()=" + getRoom() + ", getReedSwitch()="
		+ getReedSwitch() + ", getId()=" + getId() + ", getFloor()=" + getFloor() + ", isOutdoor()=" + isOutdoor() + "]";
    }
}

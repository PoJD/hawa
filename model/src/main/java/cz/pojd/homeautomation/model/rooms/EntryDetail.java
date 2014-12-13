package cz.pojd.homeautomation.model.rooms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.model.spring.EntrySpecification;
import cz.pojd.rpi.State;

/**
 * POJO holding detail of an entry
 *
 * @author Lubos Housa
 * @since Dec 13, 2014 9:20:40 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryDetail {
    private EntrySpecification specification;
    private String name;
    private State reedSwitch;

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

    public State getReedSwitch() {
	return reedSwitch;
    }

    public void setReedSwitch(State reedSwitch) {
	this.reedSwitch = reedSwitch;
    }

    public EntryDetail() {
    }

    public EntryDetail(EntryDetail copy) {
	setSpecification(copy.getSpecification());
	setName(copy.getName());
	setReedSwitch(copy.getReedSwitch());
    }

    public EntryDetail(Entry entry) {
	setSpecification(entry.getSpecification());
	setName(entry.getName());
	setReedSwitch(entry.getReedSwitch().getState());
    }

    @Override
    public String toString() {
	return "EntryDetail [specification=" + specification + ", name=" + name + ", reedSwitch=" + reedSwitch + "]";
    }
}

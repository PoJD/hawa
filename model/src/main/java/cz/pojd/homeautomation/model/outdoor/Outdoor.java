package cz.pojd.homeautomation.model.outdoor;

import java.util.List;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.homeautomation.model.lights.LightCapable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Outdoor entity
 *
 * @author Lubos Housa
 * @since Sep 9, 2014 11:32:45 PM
 */
public class Outdoor extends LightCapable {

    public static final String NAME = "Outdoor";

    private List<Sensor> sensors;

    public List<Sensor> getSensors() {
	return sensors;
    }

    public void setSensors(List<Sensor> outdoorSensors) {
	this.sensors = outdoorSensors;
    }

    @Override
    public String getName() {
	return NAME;
    }

    public OutdoorDetail getLastDetail() {
	return (OutdoorDetail) super.getLastDetail();
    }

    @Override
    public String getId() {
	return getName();
    }

    @Override
    public Floor getFloor() {
	return Floor.Basement;
    }

    @Override
    public boolean isOutdoor() {
	return true;
    }

    @Override
    public String toString() {
	return "Outdoor [getSensors()=" + getSensors() + ", getName()=" + getName() + ", getLastDetail()=" + getLastDetail() + ", getId()=" + getId()
		+ ", getFloor()=" + getFloor() + ", isOutdoor()=" + isOutdoor() + ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()="
		+ getLightSwitch() + ", getLightControl()=" + getLightControl() + ", getLightLevelSensor()=" + getLightLevelSensor() + "]";
    }
}

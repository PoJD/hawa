package cz.pojd.homeautomation.hawa.outdoor;

import java.util.List;

import cz.pojd.homeautomation.hawa.lights.LightCapable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Outdoor entity
 *
 * @author Lubos Housa
 * @since Sep 9, 2014 11:32:45 PM
 */
public class Outdoor extends LightCapable {

    private List<Sensor> sensors;

    public List<Sensor> getSensors() {
	return sensors;
    }

    public void setSensors(List<Sensor> outdoorSensors) {
	this.sensors = outdoorSensors;
    }

    @Override
    public String getName() {
	return "Outdoor";
    }

    @Override
    public String toString() {
	return "Outdoor [getSensors()=" + getSensors() + ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()=" + getLightSwitch()
		+ ", getLightControl()=" + getLightControl() + "]";
    }
}

package cz.pojd.homeautomation.hawa.outdoor;

import java.util.List;

import javax.inject.Inject;

import cz.pojd.homeautomation.hawa.AutolightsCapable;
import cz.pojd.rpi.sensors.Sensor;

/**
 * Outdoor entity
 *
 * @author Lubos Housa
 * @since Sep 9, 2014 11:32:45 PM
 */
public class Outdoor extends AutolightsCapable {

    private final List<Sensor> outdoorSensors;

    @Inject
    public Outdoor(List<Sensor> outdoorSensors) {
	this.outdoorSensors = outdoorSensors;
    }

    public List<Sensor> getOutdoorSensors() {
	return outdoorSensors;
    }

    @Override
    public String toString() {
	return "Outdoor [outdoorSensors=" + outdoorSensors + ", autoLights=" + getAutoLights() + "]";
    }
}

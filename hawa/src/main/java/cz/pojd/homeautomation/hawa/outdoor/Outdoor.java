package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.rpi.sensors.Reading;

/**
 * Simple POJO holding the outdoor state.
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 3:58:02 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Outdoor {

    private final List<Reading> sensorReadings = new ArrayList<>();
    private boolean autoLights = true;

    public boolean isAutoLights() {
	return autoLights;
    }

    public void setAutoLights(boolean autoLights) {
	this.autoLights = autoLights;
    }

    public List<Reading> getSensorReadings() {
	return sensorReadings;
    }

    public void reset() {
	sensorReadings.clear();
    }

    public void addReading(Reading reading) {
	sensorReadings.add(reading);
    }

    public void addAllReadings(Collection<Reading> readings) {
	sensorReadings.addAll(readings);
    }

    @Override
    public String toString() {
	return "OutdoorState [sensorReadings=" + sensorReadings + ", autoLights=" + autoLights + "]";
    }
}

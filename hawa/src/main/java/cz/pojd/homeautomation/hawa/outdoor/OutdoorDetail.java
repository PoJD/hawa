package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.rpi.sensors.Reading;

/**
 * Simple POJO holding the outdoor detail.
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 3:58:02 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutdoorDetail {

    private List<Reading> sensorReadings = new ArrayList<>();
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

    public void setSensorReadings(List<Reading> sensorReadings) {
	this.sensorReadings = sensorReadings;
    }

    @Override
    public String toString() {
	return "OutdoorDetail [sensorReadings=" + sensorReadings + ", autoLights=" + autoLights + "]";
    }
}

package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.hawa.LightCapableDetail;
import cz.pojd.rpi.sensors.Reading;

/**
 * Simple POJO holding the outdoor detail.
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 3:58:02 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutdoorDetail extends LightCapableDetail {

    private List<Reading> sensorReadings = new ArrayList<>();

    public OutdoorDetail() {
    }

    public OutdoorDetail(Outdoor outdoor, List<Reading> sensorReadings) {
	super(outdoor);
	this.sensorReadings = sensorReadings;
    }

    public List<Reading> getSensorReadings() {
	return sensorReadings;
    }

    @Override
    public String toString() {
	return "OutdoorDetail [getSensorReadings()=" + getSensorReadings() + ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()="
		+ getLightSwitch() + ", getLightControl()=" + getLightControl() + "]";
    }
}

package cz.pojd.homeautomation.hawa.outdoor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.hawa.refresh.RefreshedLightCapableDetail;
import cz.pojd.rpi.sensors.Reading;

/**
 * Simple POJO holding the outdoor detail.
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 3:58:02 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutdoorDetail extends RefreshedLightCapableDetail {

    private List<Reading> sensorReadings = new ArrayList<>();

    public OutdoorDetail() {
    }

    public OutdoorDetail(Outdoor outdoor) {
	super(outdoor);
    }

    public List<Reading> getSensorReadings() {
	return sensorReadings;
    }

    public void setSensorReadings(List<Reading> sensorReadings) {
	this.sensorReadings = sensorReadings;
    }

    @Override
    public String toString() {
	return "OutdoorDetail [getSensorReadings()=" + getSensorReadings() + ", getLastUpdate()=" + getLastUpdate() + ", getMotionSensor()="
		+ getMotionSensor() + ", getLightSwitch()=" + getLightSwitch() + ", getLightControl()=" + getLightControl() + "]";
    }
}

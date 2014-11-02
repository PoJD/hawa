package cz.pojd.homeautomation.model.outdoor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.model.graphs.GraphData;
import cz.pojd.homeautomation.model.refresh.RefreshedLightCapableDetail;
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
    private GraphData[] outdoorHistory;

    public OutdoorDetail() {
    }

    public OutdoorDetail(OutdoorDetail copy) {
	super(copy);
	setSensorReadings(copy.getSensorReadings());
	setOutdoorHistory(copy.getOutdoorHistory());
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

    public GraphData[] getOutdoorHistory() {
	return outdoorHistory;
    }

    public void setOutdoorHistory(GraphData[] outdoorHistory) {
	this.outdoorHistory = outdoorHistory;
    }

    @Override
    public String toString() {
	return "OutdoorDetail [getSensorReadings()=" + getSensorReadings() + ", getOutdoorHistory()=" + Arrays.toString(getOutdoorHistory())
		+ ", getLastUpdate()=" + getLastUpdate() + ", getMotionSensor()=" + getMotionSensor() + ", getLightSwitch()=" + getLightSwitch()
		+ ", getLightControl()=" + getLightControl() + ", getLightLevel()=" + getLightLevel() + "]";
    }
}

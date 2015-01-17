package cz.pojd.homeautomation.model.outdoor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.pojd.homeautomation.model.refresh.RefreshedLightCapableDetail;
import cz.pojd.homeautomation.model.web.GraphData;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;

/**
 * Simple POJO holding the outdoor detail.
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 3:58:02 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutdoorDetail extends RefreshedLightCapableDetail {

    private Map<Type, Reading> sensorReadings = new HashMap<>();
    private GraphData[] outdoorHistory;

    public OutdoorDetail() {
    }

    public OutdoorDetail(OutdoorDetail copy) {
	super(copy);
	sensorReadings = copy.sensorReadings;
	outdoorHistory = copy.outdoorHistory;
    }

    public OutdoorDetail(Outdoor outdoor) {
	super(outdoor);
	if (outdoor.getLastDetail() != null) {
	    // take the last readings - all should be valid
	    sensorReadings = outdoor.getLastDetail().sensorReadings;
	}
    }

    public void setSensorReadings(Reading... readings) {
	for (Reading reading : readings) {
	    if (reading.isValid()) {
		sensorReadings.put(reading.getType(), reading);
	    }
	}
    }

    public Collection<Reading> getSensorReadings() {
	return sensorReadings.values();
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

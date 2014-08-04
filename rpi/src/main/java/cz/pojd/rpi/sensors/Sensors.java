package cz.pojd.rpi.sensors;

import java.util.List;

/**
 * A generic API to get all sensors. More off a workaround since Jersey Spring3 cannot inject list of beans
 * 
 * @author Lubos Housa
 * @since Aug 4, 2014 10:17:21 PM
 */
public class Sensors {

    private final List<Sensor> weatherSensors;

    public Sensors(List<Sensor> weatherSensors) {
	this.weatherSensors = weatherSensors;
    }

    public List<Sensor> getWeatherSensors() {
	return weatherSensors;
    }

}

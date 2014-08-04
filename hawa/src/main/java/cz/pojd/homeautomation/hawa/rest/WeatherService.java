package cz.pojd.homeautomation.hawa.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;

/**
 * Weather service detects the current weather conditions
 * 
 * @author Lubos Housa
 * @since Aug 3, 2014 1:09:28 AM
 */
@Path("/weather")
public class WeatherService {
    private static final Log LOG = LogFactory.getLog(WeatherService.class);

    @Inject
    private Sensors weatherSensors;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reading> getSensorReadings() {
	List<Reading> result = new ArrayList<>();
	LOG.info("Detecting current weather conditions...");

	for (Sensor sensor : weatherSensors.getWeatherSensors()) {
	    result.addAll(sensor.readAll());
	}

	if (LOG.isDebugEnabled()) {
	    LOG.debug("List of readings: " + result);
	}
	return result;
    }
}

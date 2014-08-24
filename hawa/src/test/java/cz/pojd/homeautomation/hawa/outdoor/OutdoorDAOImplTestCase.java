package cz.pojd.homeautomation.hawa.outdoor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.Sensors;

public class OutdoorDAOImplTestCase {

    private OutdoorDAOImpl dao;

    @Mocked
    private JdbcTemplate jdbcTemplate;

    @Mocked
    private Control outdoorLightControl;
    private List<Sensor> sensors;

    @Mocked
    private Sensor sensor, sensor2;

    @Before
    public void setup() {
	sensors = new ArrayList<>();
	Sensors outdoorReadSensors = new Sensors(sensors);
	sensors.add(sensor);

	dao = new OutdoorDAOImpl();
	dao.setOutdoorLightControl(outdoorLightControl);
	dao.setOutdoorReadSensors(outdoorReadSensors);
	dao.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testSaveStateNoReadingBeforeShouldNotCallJdbc() {
	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(anyString, withInstanceLike(new ArrayList<Object[]>()));
		times = 0;
	    }
	};
	dao.saveState(new Date());
    }

    @Test
    public void testSaveStateReadingsBeforeShouldCallJdbc() {
	detectState();

	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(withSubstring("insert into outdoor(name, at, reading)"), withArgThat(new BaseMatcher<List<Object[]>>() {
		    @Override
		    public boolean matches(Object item) {
			@SuppressWarnings("unchecked")
			List<Object[]> argsAll = (List<Object[]>) item;
			if (argsAll.size() != 1 || argsAll.get(0).length != 3) {
			    return false;
			}
			Object[] args = argsAll.get(0);
			if (!("pressure".equals(args[0]))) {
			    return false;
			}
			if (!(args[1] instanceof Date)) {
			    return false;
			}
			if (!(args[2] instanceof Double)) {
			    return false;
			}
			if (Double.compare(5., (Double) args[2]) != 0) {
			    return false;
			}

			return true;
		    }

		    @Override
		    public void describeTo(Description description) {
			description.appendText("with argument list that contains attributes of the outdoor ");
		    }
		}));
		times = 1;
	    }
	};

	dao.saveState(new Date());
    }

    @Test
    public void testDetectStateShouldReadAllSensors() {
	sensors.add(sensor2);
	new NonStrictExpectations() {
	    {
		sensor.readAll();
		result = Collections.singletonList(Reading.newBuilder().doubleValue(5).type(Type.pressure).build());
		times = 1;

		sensor2.readAll();
		result = Collections.singletonList(Reading.newBuilder().doubleValue(50).type(Type.humidity).build());
		times = 1;
	    }
	};
	dao.detectState();

	// just confirm the readings got there (values checked in different method)
	assertEquals(2, dao.get().getSensorReadings().size());
    }

    @Test
    public void testGetNoStateDetectedShouldGetNoReadings() {
	Outdoor outdoor = dao.get();

	assertNotNull(outdoor.getSensorReadings());
	assertTrue(outdoor.getSensorReadings().isEmpty());
    }

    @Test
    public void testGetStateDetectedShouldReturnOK() {
	detectState();

	Outdoor outdoor = dao.get();
	List<Reading> list = outdoor.getSensorReadings();
	assertEquals(1, list.size());
	Reading r = list.get(0);
	assertEquals(5., r.getDoubleValue(), 0.001);
	assertEquals("5.00Pa", r.getStringValue());
	assertEquals(Type.pressure, r.getType());
    }

    @Test
    public void testSaveAutolightsOn() {
	new NonStrictExpectations() {
	    {
		outdoorLightControl.enable();
		times = 1;
	    }
	};

	Outdoor outdoor = new Outdoor();
	outdoor.setAutoLights(true);

	dao.save(outdoor);

	assertEquals(true, dao.get().isAutoLights());
    }

    @Test
    public void testSaveAutolightsOff() {
	new NonStrictExpectations() {
	    {
		outdoorLightControl.disable();
		times = 1;
	    }
	};

	Outdoor outdoor = new Outdoor();
	outdoor.setAutoLights(false);

	dao.save(outdoor);

	assertEquals(false, dao.get().isAutoLights());
    }

    private void detectState() {
	new NonStrictExpectations() {
	    {
		sensor.readAll();
		result = Collections.singletonList(Reading.newBuilder().doubleValue(5).type(Type.pressure).units("Pa").build());
		times = 1;
	    }
	};

	// we do want to test getAll in isolation - we could call refresh here, but the tests would be too dependent on each other then, so we use the
	// fact the below is protected
	dao.detectState();
    }
}

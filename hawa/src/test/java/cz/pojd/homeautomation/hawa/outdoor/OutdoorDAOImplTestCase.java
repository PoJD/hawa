package cz.pojd.homeautomation.hawa.outdoor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;
import cz.pojd.rpi.sensors.observable.ObservableSensor;

public class OutdoorDAOImplTestCase {

    private OutdoorDAOImpl dao;

    @Mocked
    private JdbcTemplate jdbcTemplate;
    private List<Sensor> sensors;

    @Mocked
    private Sensor sensor, sensor2;

    private Outdoor outdoor;
    private OutdoorDetail outdoorDetail;
    private ObservableSensor mockSensor;

    @Before
    public void setup() {
	mockSensor = new ObservableSensor() {
	    public List<Reading> readAll() {
		return null;
	    }
	    public Reading read() {
		return null;
	    }
	};
	sensors = new ArrayList<>();
	sensors.add(sensor);
	outdoor = new Outdoor(sensors);
	outdoor.setAutoLights(mockSensor);
	outdoorDetail = new OutdoorDetail();

	dao = new OutdoorDAOImpl();
	dao.setOutdoor(outdoor);
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
	OutdoorDetail outdoorDetail = dao.get();

	assertNotNull(outdoorDetail.getSensorReadings());
	assertTrue(outdoorDetail.getSensorReadings().isEmpty());
    }

    @Test
    public void testGetStateDetectedShouldReturnOK() {
	detectState();

	OutdoorDetail outdoorDetail = dao.get();
	List<Reading> list = outdoorDetail.getSensorReadings();
	assertEquals(1, list.size());
	Reading r = list.get(0);
	assertEquals(5., r.getDoubleValue(), 0.001);
	assertEquals("5.00Pa", r.getStringValue());
	assertEquals(Type.pressure, r.getType());
    }

    @Test
    public void testSaveAutolightsOn() {
	outdoorDetail.setAutoLights(true);
	dao.save(outdoorDetail);

	assertEquals(true, dao.get().isAutoLights());
	assertEquals(true, outdoor.getAutoLightsEnabled());
    }

    @Test
    public void testSaveAutolightsOff() {
	outdoorDetail.setAutoLights(false);
	dao.save(outdoorDetail);

	assertEquals(false, dao.get().isAutoLights());
	assertEquals(false, outdoor.getAutoLightsEnabled());
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

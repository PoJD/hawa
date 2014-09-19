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

import cz.pojd.homeautomation.hawa.outdoor.factory.OutdoorFactory;
import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.homeautomation.hawa.spring.OutdoorSpecification;
import cz.pojd.rpi.MockControl;
import cz.pojd.rpi.MockObservableSensor;
import cz.pojd.rpi.State;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.Sensor;

public class OutdoorDAOImplTestCase {

    private static final String INSERT_SQL_STATEMENT = "insert SQL statement";

    private OutdoorDAOImpl dao;

    @Mocked
    private JdbcTemplate jdbcTemplate;
    private List<Sensor> sensors;
    @Mocked
    private Sensor sensor, sensor2;

    @Mocked
    private Refresher refresher;

    private Outdoor outdoor;
    private OutdoorDetail detail;

    private MockObservableSensor mockedLightSwitch, mockedMotionSensor;
    private Control mockControl;

    @Before
    public void setup() {
	mockedLightSwitch = new MockObservableSensor();
	mockedMotionSensor = new MockObservableSensor();
	mockControl = new MockControl();

	sensors = new ArrayList<>();
	sensors.add(sensor);

	detail = new OutdoorDetail();
	detail.setMotionSensor(State.newBuilder().build());
	detail.setLightControl(State.newBuilder().build());
	detail.setLightSwitch(State.newBuilder().build());


	outdoor = new Outdoor();
	outdoor.setSensors(sensors);
	outdoor.setMotionSensor(mockedMotionSensor);
	outdoor.setLightControl(mockControl);
	outdoor.setLightSwitch(mockedLightSwitch);
	outdoor.setLastDetail(detail);

	dao = new OutdoorDAOImpl(new OutdoorFactory() {
	    public Outdoor create(OutdoorSpecification outdoorSpecification) {
		return outdoor;
	    }
	}, new OutdoorSpecification());
	dao.setJdbcTemplate(jdbcTemplate);
	dao.setRefresher(refresher);
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
		jdbcTemplate.batchUpdate(withEqual(INSERT_SQL_STATEMENT), withArgThat(new BaseMatcher<List<Object[]>>() {
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

	dao.setInsertSql(INSERT_SQL_STATEMENT);
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
    public void testGetShouldHaveLightDetailsSet() {
	detectState();

	OutdoorDetail outdoorDetail = dao.get();
	assertNotNull(outdoorDetail);
	assertNotNull(outdoorDetail.getMotionSensor());
	assertNotNull(outdoorDetail.getLightControl());
	assertNotNull(outdoorDetail.getLightSwitch());
    }

    @Test
    public void testSaveMotionSensorOn() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(true).build());
	dao.save(detail);

	assertNotNull(dao.get().getMotionSensor());
	assertEquals(true, dao.get().getMotionSensor().isEnabled());
    }

    @Test
    public void testSaveMotionSensorOff() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(false).build());
	dao.save(detail);

	assertNotNull(dao.get().getMotionSensor());
	assertEquals(false, dao.get().getMotionSensor().isEnabled());
    }

    @Test
    public void testSaveShouldChangeLightControlAndSwitchToo() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(true).build());
	detail.setLightControl(State.newBuilder().enabled(true).switchedOn(true).build());
	detail.setLightSwitch(State.newBuilder().enabled(true).build());
	dao.save(detail);

	assertEquals(true, dao.get().getMotionSensor().isEnabled());
	assertEquals(true, dao.get().getLightControl().isEnabled());
	// for light control we are storing state too, not only enabling/disabling
	assertEquals(true, dao.get().getLightControl().isSwitchedOn());
	assertEquals(true, dao.get().getLightSwitch().isEnabled());
    }

    @Test
    public void testSaveShouldChangeOutdoorToo() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(true).build());
	detail.setLightControl(State.newBuilder().enabled(true).switchedOn(true).build());
	detail.setLightSwitch(State.newBuilder().enabled(true).build());
	dao.save(detail);

	detectState();

	assertEquals(true, dao.get().getMotionSensor().isEnabled());
	assertEquals(true, dao.get().getLightControl().isEnabled());
	// for light control we are storing state too, not only enabling/disabling
	assertEquals(true, dao.get().getLightControl().isSwitchedOn());
	assertEquals(true, dao.get().getLightSwitch().isEnabled());
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

    // TODO add tests to cover get with history
}

package cz.pojd.homeautomation.hawa.rooms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import cz.pojd.homeautomation.hawa.MockControl;
import cz.pojd.homeautomation.hawa.MockObservableSensor;
import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.homeautomation.hawa.rooms.factory.RoomFactory;
import cz.pojd.homeautomation.hawa.spring.RoomSpecification;
import cz.pojd.rpi.State;
import cz.pojd.rpi.controls.Control;
import cz.pojd.rpi.sensors.Reading;
import cz.pojd.rpi.sensors.Reading.Type;
import cz.pojd.rpi.sensors.observable.ObservableSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

public class RoomsDAOImplTestCase {

    private static final String ROOM_NAME = RoomSpecification.HALL_DOWN.getName();
    private RoomsDAOImpl dao;
    private RoomDetail detail;

    @Mocked
    private RuntimeExecutor runtimeExecutor;
    @Mocked
    private JdbcTemplate jdbcTemplate;
    @Mocked
    private Refresher refresher;
    @Mocked
    private RoomFactory mockRoomFactory;

    private MockObservableSensor mockedLightSwitch, mockedMotionSensor, mockedTemperatureSensor;
    private Control mockControl;

    @Before
    public void setup() {
	detail = new RoomDetail();
	detail.setName(ROOM_NAME);
	detail.setMotionSensor(State.newBuilder().build());
	detail.setLightControl(State.newBuilder().build());
	detail.setLightSwitch(State.newBuilder().build());

	mockedLightSwitch = new MockObservableSensor();
	mockedMotionSensor = new MockObservableSensor();
	mockedTemperatureSensor = new MockObservableSensor();
	mockControl = new MockControl();

	createDAO(RoomSpecification.HALL_DOWN);
    }

    @Test
    public void testGetAllRoomsEmptyShouldReturnEmptyList() {
	createDAO(new RoomSpecification[] {});
	assertTrue(dao.query().isEmpty());
    }

    @Test
    public void testGetAllNoDetectStateCalledShouldReturnEmptyList() {
	List<RoomDetail> result = dao.query();
	assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllDetectStateCalledShouldReturnValidList() {
	detectState();

	List<RoomDetail> result = dao.query();
	assertEquals(1, result.size());

	RoomDetail detail = result.get(0);
	checkDetail(detail);
    }

    @Test
    public void testSaveShouldChangeMotionSensorOff() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(false).build());
	dao.save(detail);

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(false, list.get(0).getMotionSensor().isEnabled());
    }

    @Test
    public void testSaveShouldChangeMotionSensorOn() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(true).build());
	dao.save(detail);

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(true, list.get(0).getMotionSensor().isEnabled());
    }

    @Test
    public void testSaveShouldChangeLightControlAndSwitchToo() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(true).build());
	detail.setLightControl(State.newBuilder().enabled(true).switchedOn(true).build());
	detail.setLightSwitch(State.newBuilder().enabled(true).build());
	dao.save(detail);

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(true, list.get(0).getMotionSensor().isEnabled());
	assertEquals(true, list.get(0).getLightControl().isEnabled());
	// for light control we are storing state too, not only enabling/disabling
	assertEquals(true, list.get(0).getLightControl().isSwitchedOn());
	assertEquals(true, list.get(0).getLightSwitch().isEnabled());
    }

    @Test
    public void testSaveShouldChangeRoomToo() {
	detectState();
	detail.setMotionSensor(State.newBuilder().enabled(true).build());
	detail.setLightControl(State.newBuilder().enabled(true).switchedOn(true).build());
	detail.setLightSwitch(State.newBuilder().enabled(true).build());

	dao.save(detail);

	// we now mimic another detect state - yet the above should still be stored (we know it is done via saving to room dirctly)
	detectState();

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(true, list.get(0).getMotionSensor().isEnabled());
	assertEquals(true, list.get(0).getLightControl().isEnabled());
	// for light control we are storing state too, not only enabling/disabling
	assertEquals(true, list.get(0).getLightControl().isSwitchedOn());
	assertEquals(true, list.get(0).getLightSwitch().isSwitchedOn());
    }

    @Test
    public void testSaveRoomsEmptyShouldNotFail() {
	createDAO(new RoomSpecification[] {});
	dao.save(detail);
    }

    @Test
    public void testSaveRoomstateNullNameShouldNotFail() {
	detail.setName(null);
	try {
	    dao.save(detail);
	} catch (Exception e) {
	    fail("Unexpected exception:" + e.getMessage());
	}
    }

    @Test
    public void testSaveStateShouldCallJdbc() {
	detectState();

	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(anyString, withArgThat(new BaseMatcher<List<Object[]>>() {
		    @Override
		    public boolean matches(Object item) {
			@SuppressWarnings("unchecked")
			List<Object[]> argsAll = (List<Object[]>) item;
			if (argsAll.size() != 1 || argsAll.get(0).length != 3) {
			    return false;
			}
			Object[] args = argsAll.get(0);
			if (!(ROOM_NAME.equals(args[0]))) {
			    return false;
			}
			if (!(args[1] instanceof Date)) {
			    return false;
			}
			if (!(args[2] instanceof Double)) {
			    return false;
			}
			if (Double.compare(1., (Double) args[2]) != 0) {
			    return false;
			}

			return true;
		    }

		    @Override
		    public void describeTo(Description description) {
			description.appendText("with argument list that contains attributes of the room state ");
		    }
		}));
		times = 1;
	    }
	};

	dao.saveState(new Date());
    }

    @Test
    public void testSaveStateRoomsEmptyShouldNotCallJdbc() {
	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(anyString, withInstanceLike(new ArrayList<Object[]>()));
		times = 0;
	    }
	};
	dao = new RoomsDAOImpl(mockRoomFactory, new RoomSpecification[] {}, runtimeExecutor);
	dao.saveState(new Date());
    }

    @Test
    public void testSaveStateShouldNotSaveInvalidTemperature() {
	createDAO(new RoomSpecification[] { RoomSpecification.HALL_DOWN, RoomSpecification.HALL_UP, RoomSpecification.WC_BEDROOM },
		new ObservableSensor[] { mockedTemperatureSensor, mockedTemperatureSensor, new MockObservableSensor() {
		    public Reading read() {
			return Reading.invalid(Type.temperatureB);
		    }
		} });

	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(anyString, withArgThat(new BaseMatcher<List<Object[]>>() {

		    @Override
		    public boolean matches(Object item) {
			if (!(item instanceof List<?>)) {
			    return false;
			}
			List<?> unTypedList = (List<?>) item;
			if (unTypedList.size() != 2) {
			    return false;
			}
			if (!(unTypedList.get(0) instanceof Object[])) {
			    return false;
			}
			Object[] row = (Object[]) unTypedList.get(0);
			if (row.length != 3) {
			    return false;
			}
			if (!(row[2] instanceof Double)) {
			    return false;
			}
			if (Double.compare(1., (double) row[2]) != 0) {
			    return false;
			}

			return true;
		    }

		    @Override
		    public void describeTo(Description description) {
			description.appendText("list that contains exactly two arrays of 3 objects, where the last is a specific double");
		    }
		}));
		times = 1;
	    }
	};

	dao.detectState();
	dao.saveState(new Date());
    }

    @Test
    public void testDetectStateShouldReadTemperatureForEachRoom() {
	createDAO(RoomSpecification.HALL_DOWN, RoomSpecification.HALL_UP);
	dao.detectState();

	assertEquals(2, mockedTemperatureSensor.getReadCount());
    }

    @Test
    public void testRefreshDetectOKSaveOK() {
	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(anyString, withInstanceLike(new ArrayList<Object[]>()));
		result = new int[] { 1 };
		times = 1;
	    }
	};

	dao.refresh(new LocalDateTime());

	assertEquals(1, mockedTemperatureSensor.getReadCount());
    }

    @Test
    public void testGetNoDetectRoomReturnsNull() {
	assertNull(dao.get(ROOM_NAME));
    }

    @Test
    public void testGetUnknownRoomReturnsNull() {
	detectState();
	assertNull(dao.get(ROOM_NAME + "x"));
    }

    @Test
    public void testGetNoGraphData() {
	detectState();

	new NonStrictExpectations() {
	    {
		jdbcTemplate.queryForList(anyString, any);
		result = new ArrayList<>();
	    }
	};
	RoomDetail detail = dao.get(ROOM_NAME);
	checkDetail(detail);
	assertNotNull(detail.getTemperatureHistory());
	assertEquals(1, detail.getTemperatureHistory().length);
	assertNotNull(detail.getTemperatureHistory()[0].getValues());
	assertEquals(0, detail.getTemperatureHistory()[0].getValues().length);
    }

    @Test
    public void testGetSomeGraphData() {
	detectState();

	final Map<String, Object> row = new HashMap<String, Object>();
	row.put("at", new Date());
	row.put("temperature", 28.);
	new NonStrictExpectations() {
	    {
		jdbcTemplate.queryForList(anyString, (Object[]) any);
		returns(Collections.singletonList(row), new ArrayList<>());
	    }
	};
	RoomDetail detail = dao.get(ROOM_NAME);
	checkDetail(detail);
	assertNotNull(detail.getTemperatureHistory());
	assertEquals(1, detail.getTemperatureHistory().length);
	assertNotNull(detail.getTemperatureHistory()[0].getValues());
	assertEquals(1, detail.getTemperatureHistory()[0].getValues().length);
	assertEquals(2, detail.getTemperatureHistory()[0].getValues()[0].length);
	assertEquals(28., (double) detail.getTemperatureHistory()[0].getValues()[0][1], 0.001);
    }

    @Test(expected = RoomsDAOException.class)
    public void testGetGraphDataSqlError() {
	detectState();

	new NonStrictExpectations() {
	    {
		jdbcTemplate.queryForList(anyString, (Object[]) any);
		result = new RuntimeException("Some error ocurred while calling JDBC here...");
	    }
	};
	dao.get(ROOM_NAME);
    }

    @Test
    public void testGetShouldHaveLightDetailsSet() {
	detectState();

	RoomDetail roomDetail = dao.get(ROOM_NAME);
	assertNotNull(roomDetail);
	assertNotNull(roomDetail.getMotionSensor());
	assertNotNull(roomDetail.getLightControl());
	assertNotNull(roomDetail.getLightSwitch());
    }

    private void detectState() {
	// we do want to test getAll in isolation - we could call refresh here, but the tests would be too dependent on each other then, so we use the
	// fact the below is protected
	mockedTemperatureSensor.setReadCount(0);
	dao.detectState();

	// confirm the sensor read something
	assertEquals(1, mockedTemperatureSensor.getReadCount());
    }

    private void checkDetail(RoomDetail detail) {
	assertTrue(detail.getMotionSensor().isEnabled());
	assertEquals(Floor.BASEMENT, detail.getFloor());
	assertEquals(ROOM_NAME, detail.getName());
	assertEquals(1.0, detail.getTemperature().getDoubleValue(), 0.001);
	assertEquals("1.00", detail.getTemperature().getStringValue());
    }

    private void createDAO(RoomSpecification... rooms) {
	createDAO(rooms, null);
    }

    private void createDAO(RoomSpecification[] rooms, final ObservableSensor[] temperatureSensor) {
	dao = new RoomsDAOImpl(new RoomFactory() {
	    int index = 0;

	    public Room create(RoomSpecification specification) {
		Room result = new Room();
		result.setFloor(specification.getFloor() != null ? specification.getFloor() : Floor.BASEMENT);
		result.setName(specification.getName());
		result.setMotionSensor(mockedMotionSensor);
		result.setLightControl(mockControl);
		result.setLightSwitch(mockedLightSwitch);
		result.setTemperatureSensor(temperatureSensor != null ? temperatureSensor[index++] : mockedTemperatureSensor);
		return result;
	    }
	}, rooms, runtimeExecutor);
	dao.setJdbcTemplate(jdbcTemplate);
	dao.setRefresher(refresher);
    }
}

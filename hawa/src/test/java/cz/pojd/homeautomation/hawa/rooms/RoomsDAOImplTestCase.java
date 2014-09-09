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

import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.rpi.sensors.observable.ObservableSensor;
import cz.pojd.rpi.system.RuntimeExecutor;

public class RoomsDAOImplTestCase {

    private static final String ROOM_NAME = "test";
    private RoomsDAOImpl dao;
    private RoomDetail detail;

    @Mocked
    private RuntimeExecutor runtimeExecutor;
    @Mocked
    private JdbcTemplate jdbcTemplate;
    @Mocked
    private Refresher refresher;
    @Mocked
    private ObservableSensor mockSensor;

    @Before
    public void setup() {
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(RoomSpecification.newBuilder().autolights(mockSensor).floor(Floor.BASEMENT).name(ROOM_NAME).temperatureID("id").build());
	createDAO(rooms);

	detail = new RoomDetail();
	detail.setName(ROOM_NAME);
    }

    @Test
    public void testGetAllRoomsEmptyShouldReturnEmptyList() {
	createDAO(new ArrayList<RoomSpecification>());
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
    public void testSaveShouldChangeAutolightsOff() {
	detectState();
	detail.setAutoLights(false);
	dao.save(detail);

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(false, list.get(0).getAutoLights());
    }

    @Test
    public void testSaveShouldChangeAutolightsOn() {
	detectState();
	detail.setAutoLights(true);
	dao.save(detail);

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(true, list.get(0).getAutoLights());
    }

    @Test
    public void testSaveRoomsEmptyShouldNotFail() {
	createDAO(new ArrayList<RoomSpecification>());
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
			if (Double.compare(22., (Double) args[2]) != 0) {
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
	dao = new RoomsDAOImpl(new ArrayList<RoomSpecification>(), runtimeExecutor);
	dao.saveState(new Date());
    }

    @Test
    public void testSaveStateShouldNotSaveInvalidTemperature() {
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(RoomSpecification.newBuilder().autolights(mockSensor).floor(Floor.BASEMENT).name(ROOM_NAME).temperatureID("id").build());
	rooms.add(RoomSpecification.newBuilder().autolights(mockSensor).floor(Floor.BASEMENT).name("test2").temperatureID("id2").build());
	rooms.add(RoomSpecification.newBuilder().autolights(mockSensor).floor(Floor.BASEMENT).name("test3").temperatureID("id3").build());
	createDAO(rooms);

	new NonStrictExpectations() {
	    {
		runtimeExecutor.execute(anyString);
		returns(Collections.singletonList(0.), Collections.singletonList(50000.), Collections.singletonList(22000.));
		jdbcTemplate.batchUpdate(anyString, withArgThat(new BaseMatcher<List<Object[]>>() {

		    @Override
		    public boolean matches(Object item) {
			if (!(item instanceof List<?>)) {
			    return false;
			}
			List<?> unTypedList = (List<?>) item;
			if (unTypedList.size() != 1) {
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
			if (Double.compare(22., (double) row[2]) != 0) {
			    return false;
			}

			return true;
		    }

		    @Override
		    public void describeTo(Description description) {
			description.appendText("list that contains exactly one array of 3 objects, where the last is a specific double");
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
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(RoomSpecification.newBuilder().autolights(mockSensor).floor(Floor.BASEMENT).name(ROOM_NAME).temperatureID("id").build());
	rooms.add(RoomSpecification.newBuilder().autolights(mockSensor).floor(Floor.BASEMENT).name("test2").temperatureID("id2").build());
	createDAO(rooms);

	new NonStrictExpectations() {
	    {
		runtimeExecutor.execute(anyString);
		result = 22000.; // our temperature will be 22°C always
		times = 2;
	    }
	};
	dao.detectState();
    }

    @Test
    public void testRefreshDetectOKSaveOK() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.execute(anyString);
		result = 22000.; // our temperature will be 22°C always
		times = 1;

		jdbcTemplate.batchUpdate(anyString, withInstanceLike(new ArrayList<Object[]>()));
		result = new int[] { 1 };
		times = 1;
	    }
	};

	dao.refresh(new LocalDateTime());
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
	assertEquals(2, detail.getTemperatureHistory().length);
	assertNotNull(detail.getTemperatureHistory()[0].getValues());
	assertEquals(0, detail.getTemperatureHistory()[0].getValues().length);
	assertNotNull(detail.getTemperatureHistory()[1].getValues());
	assertEquals(0, detail.getTemperatureHistory()[1].getValues().length);
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
	assertEquals(2, detail.getTemperatureHistory().length);
	assertNotNull(detail.getTemperatureHistory()[0].getValues());
	assertEquals(1, detail.getTemperatureHistory()[0].getValues().length);
	assertEquals(2, detail.getTemperatureHistory()[0].getValues()[0].length);
	assertEquals(28., (double) detail.getTemperatureHistory()[0].getValues()[0][1], 0.001);
	assertNotNull(detail.getTemperatureHistory()[1].getValues());
	assertEquals(0, detail.getTemperatureHistory()[1].getValues().length);
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

    private void detectState() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.execute(anyString);
		result = 22000.; // our temperature will be 22°C always
		times = 1;
	    }
	};

	// we do want to test getAll in isolation - we could call refresh here, but the tests would be too dependent on each other then, so we use the
	// fact the below is protected
	dao.detectState();
    }

    private void checkDetail(RoomDetail detail) {
	assertEquals(false, detail.getAutoLights());
	assertEquals(Floor.BASEMENT, detail.getFloor());
	assertEquals(ROOM_NAME, detail.getName());
	assertEquals(22.0, detail.getTemperature().getDoubleValue(), 0.001);
	assertEquals("22.00", detail.getTemperature().getStringValue());
    }

    private void createDAO(List<RoomSpecification> rooms) {
	dao = new RoomsDAOImpl(rooms, runtimeExecutor);
	dao.setJdbcTemplate(jdbcTemplate);
	dao.setRefresher(refresher);
    }
}

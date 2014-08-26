package cz.pojd.homeautomation.hawa.rooms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.rpi.system.RuntimeExecutor;

public class RoomsDAOImplTestCase {

    private RoomsDAOImpl dao;
    private RoomDetail state;

    @Mocked
    private RuntimeExecutor runtimeExecutor;
    @Mocked
    private JdbcTemplate jdbcTemplate;
    @Mocked
    private Refresher refresher;

    @Before
    public void setup() {
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(RoomSpecification.newBuilder().autolights(true).floor(Floor.BASEMENT).name("test").temperatureID("id").build());
	dao = new RoomsDAOImpl(rooms, runtimeExecutor);
	dao.setJdbcTemplate(jdbcTemplate);
	dao.setRefresher(refresher);

	state = new RoomDetail();
	state.setName("test");
    }

    @Test
    public void testGetAllRoomsEmptyShouldReturnEmptyList() {
	dao = new RoomsDAOImpl(new ArrayList<RoomSpecification>(), runtimeExecutor);
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

	RoomDetail state = result.get(0);
	assertEquals(true, state.getAutoLights());
	assertEquals(Floor.BASEMENT, state.getFloor());
	assertEquals("test", state.getName());
	assertEquals(22.0, state.getRawTemperature(), 0.001);
	assertEquals("22.00", state.getTemperature());
    }

    @Test
    public void testSaveShouldChangeAutolights() {
	detectState();
	state.setAutoLights(false);
	dao.save(state);

	List<RoomDetail> list = dao.query();
	assertEquals(1, list.size());
	assertEquals(false, list.get(0).getAutoLights());
    }

    @Test
    public void testSaveRoomsEmptyShouldNotFail() {
	dao = new RoomsDAOImpl(new ArrayList<RoomSpecification>(), runtimeExecutor);
	dao.save(state);
    }

    @Test
    public void testSaveRoomstateNullNameShouldNotFail() {
	state.setName(null);
	try {
	    dao.save(state);
	} catch (Exception e) {
	    fail("Unexpected exception:" + e.getMessage());
	}
    }

    @Test
    public void testSaveStateShouldCallJdbc() {
	detectState();

	new NonStrictExpectations() {
	    {
		jdbcTemplate.batchUpdate(anyString,
			withArgThat(new BaseMatcher<List<Object[]>>() {
			    @Override
			    public boolean matches(Object item) {
				@SuppressWarnings("unchecked")
				List<Object[]> argsAll = (List<Object[]>) item;
				if (argsAll.size() != 1 || argsAll.get(0).length != 3) {
				    return false;
				}
				Object[] args = argsAll.get(0);
				if (!("test".equals(args[0]))) {
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
    public void testDetectStateShouldReadTemperatureForEachRoom() {
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(RoomSpecification.newBuilder().autolights(true).floor(Floor.BASEMENT).name("test").temperatureID("id").build());
	rooms.add(RoomSpecification.newBuilder().autolights(false).floor(Floor.BASEMENT).name("test2").temperatureID("id2").build());
	dao = new RoomsDAOImpl(rooms, runtimeExecutor);
	dao.setRefresher(refresher);

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
}

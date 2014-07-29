package cz.pojd.rpi.rooms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mock DAO for rooms (handy to avoid realtime dependency on GPIO, etc)
 * 
 * @author Lubos Housa
 * @since Jul 27, 2014 6:23:59 PM
 */
public class MockRoomsDAO implements RoomsDAO {
    private static final Random r = new Random();

    public List<Room> getAll() {
	List<Room> result = new ArrayList<>();

	Room r1 = new Room();
	r1.setName("Bedroom");
	r1.setTemperature(new BigDecimal(-10 + r.nextInt(50)));
	r1.setAutoLights(false);
	Room r2 = new Room();
	r2.setName("Kitchen");
	r2.setTemperature(new BigDecimal(-10 + r.nextInt(50)));
	r2.setAutoLights(true);

	result.add(r1);
	result.add(r2);
	return result;
    }

    @Override
    public void save(Room room) {
    }
}

package cz.pojd.homeautomation.hawa.spring;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cz.pojd.homeautomation.hawa.rooms.RoomSpecification;
import cz.pojd.homeautomation.hawa.rooms.RoomsDAO;
import cz.pojd.homeautomation.hawa.rooms.RoomsDAOImpl;
import cz.pojd.rpi.spring.RpiConfig;

/**
 * Main configuration for spring
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
@Import(RpiConfig.class)
public class HawaConfig {

    @Inject
    RpiConfig rpiConfig;
    
    @Bean
    public List<RoomSpecification> rooms() {
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(new RoomSpecification("Bedroom", "28-0000060a84d1"));
	rooms.add(new RoomSpecification("Kitchen", "28-0000060a84d1"));
	return rooms;
    }

    @Bean
    public RoomsDAO roomsDAO() {
	return new RoomsDAOImpl(rooms(), rpiConfig.runtimeExecutor(), rpiConfig.barometricSensor());
    }
}

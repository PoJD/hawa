package cz.pojd.homeautomation.hawa.spring;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import cz.pojd.homeautomation.hawa.outdoor.OutdoorDAO;
import cz.pojd.homeautomation.hawa.outdoor.OutdoorDAOImpl;
import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.homeautomation.hawa.refresh.SpringScheduledRefresher;
import cz.pojd.homeautomation.hawa.rooms.Floor;
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
@EnableScheduling
public class HawaConfig {

    @Inject
    private RpiConfig rpiConfig;

    @Bean
    public List<RoomSpecification> rooms() {
	List<RoomSpecification> rooms = new ArrayList<>();
	rooms.add(RoomSpecification.newBuilder().name("Hall down").temperatureID("28-0000060a84d1").autolights(true).build());
	rooms.add(RoomSpecification.newBuilder().name("Kitchen").temperatureID("28-0000060a84d1").build());
	rooms.add(RoomSpecification.newBuilder().name("Living room").temperatureID("28-0000060a84d1").build());
	rooms.add(RoomSpecification.newBuilder().name("Bathroom down").temperatureID("28-0000060a84d1").build());
	rooms.add(RoomSpecification.newBuilder().name("WC down").temperatureID("28-0000060a84d1").build());
	rooms.add(RoomSpecification.newBuilder().name("Down room").temperatureID("28-0000060a84d1").build());
	rooms.add(RoomSpecification.newBuilder().name("Hall up").temperatureID("28-0000060a84d1").autolights(true).floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Bedroom").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Child room 1").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Child room 2").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Child room 3").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Child room 4").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Bathroom bed").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("WC bed").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Bathroom up").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("WC up").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	rooms.add(RoomSpecification.newBuilder().name("Laundry room").temperatureID("28-0000060a84d1").floor(Floor.FIRST).build());
	return rooms;
    }

    @Bean
    public RoomsDAO roomsDAO() {
	return new RoomsDAOImpl(rooms(), rpiConfig.runtimeExecutor());
    }

    @Bean
    public OutdoorDAO outdoorDAO() {
	return new OutdoorDAOImpl();
    }

    @Bean
    public Refresher refresher() {
	return new SpringScheduledRefresher(datePattern());
    }

    @Bean
    public String datePattern() {
	return "dd.MM.yyyy HH:mm";
    }
}

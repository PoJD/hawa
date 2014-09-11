package cz.pojd.homeautomation.hawa.spring;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.homeautomation.hawa.outdoor.OutdoorDAO;
import cz.pojd.homeautomation.hawa.outdoor.OutdoorDAOImpl;
import cz.pojd.homeautomation.hawa.outdoor.factory.DefaultOutdoorFactory;
import cz.pojd.homeautomation.hawa.outdoor.factory.OutdoorFactory;
import cz.pojd.homeautomation.hawa.refresh.Refresher;
import cz.pojd.homeautomation.hawa.refresh.SpringScheduledRefresher;
import cz.pojd.homeautomation.hawa.rooms.RoomsDAO;
import cz.pojd.homeautomation.hawa.rooms.RoomsDAOImpl;
import cz.pojd.homeautomation.hawa.rooms.factory.DefaultRoomFactory;
import cz.pojd.homeautomation.hawa.rooms.factory.RoomFactory;
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
    public RoomSpecification[] rooms() {
	return RoomSpecification.values();
    }

    @Bean
    public RoomFactory roomFactory() {
	// each floor lights input, then output
	return new DefaultRoomFactory(rpiConfig.getMCP23017Provider(0x20), rpiConfig.getMCP23017Provider(0x21), rpiConfig.getMCP23017Provider(0x22),
		rpiConfig.getMCP23017Provider(0x23));
    }

    @Bean
    public RoomsDAO roomsDAO() {
	return new RoomsDAOImpl(roomFactory(), rooms(), rpiConfig.runtimeExecutor());
    }

    @Bean
    public OutdoorFactory outdoorFactory() {
	return new DefaultOutdoorFactory();
    }

    @Bean
    public OutdoorDAO outdoorDAO() {
	return new OutdoorDAOImpl(outdoorFactory(), outdoorSpecification());
    }

    @Bean
    public OutdoorSpecification outdoorSpecification() {
	OutdoorSpecification result = new OutdoorSpecification();
	result.setAltitude(290);
	result.setDhtSensorSysClassPin(17);
	result.setNewRaspi(rpiConfig.newRasPI());
	result.setGpioProvider(rpiConfig.getMCP23017Provider(0x24));
	result.setMotionSensorPin(RaspiPin.GPIO_01);
	result.setLightSwitchPin(RaspiPin.GPIO_02);
	result.setLightControlPin(RaspiPin.GPIO_03);
	return result;
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

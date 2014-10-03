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
import cz.pojd.rpi.sensors.i2c.TSL2561LightSensor;
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
		rpiConfig.getMCP23017Provider(0x23), rpiConfig.newRasPI());
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
	result.setDhtSensorSysClassPin(22); // 22 is GPIO3 in Pi4J numbering
	result.setNewRaspi(rpiConfig.newRasPI());
	result.setGpioProvider(rpiConfig.gpio().getDefaultProvider());
	result.setMotionSensorPin(RaspiPin.GPIO_00);
	result.setLightSwitchPin(RaspiPin.GPIO_01);
	result.setLightControlPin(RaspiPin.GPIO_02);
	result.setLightLevelSensorAddress(TSL2561LightSensor.TSL2561_ADDRESS_FLOAT);
	result.setLightLevelTreshold(500); // TODO should be higher once outside?
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

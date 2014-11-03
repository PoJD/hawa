package cz.pojd.homeautomation.model.spring;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.pi4j.io.gpio.RaspiPin;

import cz.pojd.homeautomation.model.lights.DefaultMotionSensorLightTrigger;
import cz.pojd.homeautomation.model.outdoor.OutdoorDAO;
import cz.pojd.homeautomation.model.outdoor.OutdoorDAOImpl;
import cz.pojd.homeautomation.model.outdoor.factory.DefaultOutdoorFactory;
import cz.pojd.homeautomation.model.outdoor.factory.OutdoorFactory;
import cz.pojd.homeautomation.model.refresh.Refresher;
import cz.pojd.homeautomation.model.refresh.SpringScheduledRefresher;
import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.homeautomation.model.rooms.RoomsDAOImpl;
import cz.pojd.homeautomation.model.rooms.factory.DefaultRoomFactory;
import cz.pojd.homeautomation.model.rooms.factory.RoomFactory;
import cz.pojd.rpi.sensors.i2c.TSL2561LightSensor;
import cz.pojd.rpi.spring.RpiConfig;

/**
 * Main configuration for spring
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
@EnableScheduling
public class ModelConfig {

    @Inject
    private RpiConfig rpiConfig;

    @Bean
    public RoomSpecification[] rooms() {
	return RoomSpecification.values();
    }

    @Bean
    public RoomFactory roomFactory() {
	// each floor lights input, then output
	return new DefaultRoomFactory(rpiConfig.gpio().getDefaultProvider(), rpiConfig.gpio().getDefaultProvider(), rpiConfig.gpio()
		.getDefaultProvider(), rpiConfig.gpio().getDefaultProvider());

	// TODO uncomment below once deployed to real house
	// return new DefaultRoomFactory(rpiConfig.getMCP23017Provider(0x20), rpiConfig.getMCP23017Provider(0x21),
	// rpiConfig.getMCP23017Provider(0x22),
	// rpiConfig.getMCP23017Provider(0x23));
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
    public DefaultMotionSensorLightTrigger motionSensorLightTrigger() {
	return new DefaultMotionSensorLightTrigger(rpiConfig.gpio(), rpiConfig.newRasPI());
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
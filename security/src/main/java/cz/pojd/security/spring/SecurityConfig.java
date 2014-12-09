package cz.pojd.security.spring;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import cz.pojd.homeautomation.model.rooms.RoomDetail;
import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.homeautomation.model.spring.ModelConfig;
import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.spring.RpiConfig;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.controller.DefaultSecurityController;
import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.controller.SwitchingLightsObserver;
import cz.pojd.security.event.CalendarEventTranslator;
import cz.pojd.security.event.CalendarEventTranslatorImpl;
import cz.pojd.security.event.SecurityEventDAO;
import cz.pojd.security.event.SecurityEventDAOImpl;
import cz.pojd.security.event.SecurityEventTranslator;
import cz.pojd.security.event.SimpleStringTranslator;
import cz.pojd.security.event.SourceResolver;
import cz.pojd.security.event.SourceResolverImpl;
import cz.pojd.security.ftp.CameraUploadFtplet;
import cz.pojd.security.ftp.Ftp;
import cz.pojd.security.handler.EmailSender;
import cz.pojd.security.handler.SecurityEventStorer;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.hooks.MotionSensorSecurityHook;
import cz.pojd.security.hooks.RoomDetailSecurityObserver;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;
import cz.pojd.security.rules.RulesDAOImpl;
import cz.pojd.security.rules.impl.BasementWindowOpenedWhenHouseIsFull;
import cz.pojd.security.rules.impl.DoorOrGarageOpened;
import cz.pojd.security.rules.impl.HighTemperature;
import cz.pojd.security.rules.impl.IndoorCameraMotionWhenHouseIsEmpty;
import cz.pojd.security.rules.impl.MotionInHallsWhenHouseIsEmpty;
import cz.pojd.security.rules.impl.MotionOutside;
import cz.pojd.security.rules.impl.WindowOpened;

/**
 * Main configuration for spring in security project
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 5:26:21 PM
 */
@Configuration
@EnableScheduling
public class SecurityConfig {

    @Inject
    private ModelConfig modelConfig;
    @Inject
    private RpiConfig rpiConfig;

    @Bean
    public Ftp ftp() throws FtpException {
	return new Ftp(2222, ftpUserManager(), cameraUploadFtplet());
    }

    @Bean
    public Ftplet cameraUploadFtplet() {
	// each camera has to upload files to /camera/cameraName/ directories inside root specified in below property file (/tmp/ here)
	CameraUploadFtplet ftplet = new CameraUploadFtplet("/tmp/", "/camera/");
	ftplet.setSecurityController(securityController());
	return ftplet;
    }

    @Bean
    public UserManager ftpUserManager() throws FtpException {
	PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
	userManagerFactory.setFile(new File("ftp-users.properties"));
	userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
	return userManagerFactory.createUserManager();
    }

    @Bean
    public Rule[] rules() {
	return new Rule[] { new HighTemperature(), new MotionInHallsWhenHouseIsEmpty(), new IndoorCameraMotionWhenHouseIsEmpty(), new WindowOpened(),
		new DoorOrGarageOpened(), new MotionOutside(), new BasementWindowOpenedWhenHouseIsFull() };
    }

    @Bean
    public RulesDAO rulesDAO() {
	return new RulesDAOImpl(rules());
    }

    @Bean
    public SecurityHandler emailSender() {
	return new EmailSender();
    }

    @Bean
    public SourceResolver sourceResolver() {
	return new SourceResolverImpl(modelConfig.outdoorDAO(), modelConfig.roomsDAO());
    }

    @Bean
    public SecurityEventDAO securityEventDAO() {
	return new SecurityEventDAOImpl(modelConfig.storageCleanup(), sourceResolver());
    }

    @Bean
    public Controller securityController() {
	Controller result = new DefaultSecurityController(rpiConfig.timeService(), rulesDAO(), emailSender(), new SecurityEventStorer(
		securityEventDAO()));
	for (Observer<Controller, SecurityMode> observer : securityObservers()) {
	    result.addObserver(observer);
	}
	return result;
    }

    @Bean
    public MotionSensorSecurityHook motionSensorTrigger() {
	return new MotionSensorSecurityHook(modelConfig.roomsDAO(), modelConfig.outdoorDAO(), securityController());
    }

    @Bean
    public CalendarEventTranslator calendarEventTranslator() {
	return new CalendarEventTranslatorImpl();
    }

    @Bean
    public SecurityEventTranslator<String> stringEventTranslator() {
	return new SimpleStringTranslator();
    }

    @Bean
    public Collection<? extends Observer<Controller, SecurityMode>> securityObservers() {
	return Collections.singleton(new SwitchingLightsObserver(modelConfig.roomsDAO()));
    }

    @Bean
    public Observer<RoomsDAO, RoomDetail> roomDetailSecurityObserver() {
	RoomDetailSecurityObserver result = new RoomDetailSecurityObserver(securityController(), 35); // 35 degrees is a threshold
	modelConfig.roomsDAO().addObserver(result);
	return result;
    }
}

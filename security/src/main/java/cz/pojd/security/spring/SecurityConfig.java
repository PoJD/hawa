package cz.pojd.security.spring;

import java.io.File;

import javax.inject.Inject;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cz.pojd.homeautomation.model.spring.ModelConfig;
import cz.pojd.security.controller.Controller;
import cz.pojd.security.controller.DefaultSecurityController;
import cz.pojd.security.event.SecurityEventDAO;
import cz.pojd.security.event.SecurityEventDAOImpl;
import cz.pojd.security.ftp.CameraUploadFtplet;
import cz.pojd.security.ftp.Ftp;
import cz.pojd.security.handler.EmailSender;
import cz.pojd.security.handler.SecurityEventStorer;
import cz.pojd.security.motion.MotionSensorSecurityTrigger;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;
import cz.pojd.security.rules.RulesDAOImpl;
import cz.pojd.security.rules.impl.BasementWindowOpenedWhenHouseIsFull;
import cz.pojd.security.rules.impl.DoorOrGarageOpened;
import cz.pojd.security.rules.impl.HighTemperature;
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
@ComponentScan("cz.pojd.security")
public class SecurityConfig {

    @Inject
    private ModelConfig modelConfig;

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
	return new Rule[] { new HighTemperature(), new MotionInHallsWhenHouseIsEmpty(), new WindowOpened(), new DoorOrGarageOpened(),
		new MotionOutside(), new BasementWindowOpenedWhenHouseIsFull() };
    }

    @Bean
    public RulesDAO rulesDAO() {
	return new RulesDAOImpl(rules());
    }

    @Bean
    public SecurityEventDAO securityEventDAO() {
	return new SecurityEventDAOImpl();
    }

    @Bean
    public Controller securityController() {
	return new DefaultSecurityController(modelConfig.roomsDAO(), rulesDAO(), new EmailSender(), new SecurityEventStorer(securityEventDAO()));
    }

    @Bean
    public MotionSensorSecurityTrigger motionSensorTrigger() {
	return new MotionSensorSecurityTrigger(modelConfig.roomsDAO(), modelConfig.outdoorDAO(), securityController());
    }
}

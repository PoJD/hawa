package cz.pojd.homeautomation.hawa.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cz.pojd.homeautomation.hawa.rest.rooms.MockRoomsDAO;
import cz.pojd.homeautomation.hawa.rest.rooms.RoomsDAO;

/**
 * Main configuration for spring
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
@ComponentScan("cz.pojd.homeautomation.hawa.rest")
public class HawaConfig {

    @Bean
    public Runtime runtime() {
	return Runtime.getRuntime();
    }

    @Bean
    public List<String> fileSystems() {
	List<String> fileSystems = new ArrayList<>();
	fileSystems.add("/");
	fileSystems.add("/boot");
	fileSystems.add("/var/log");
	fileSystems.add("/tmp");
	return fileSystems;
    }
    
    @Bean
    public RoomsDAO roomsDAO() {
	return new MockRoomsDAO();
    }
}

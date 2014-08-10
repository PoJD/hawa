package cz.pojd.rpi.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cz.pojd.rpi.system.RuntimeExecutor;
import cz.pojd.rpi.system.RuntimeExecutorImpl;

/**
 * Main configuration for spring in rpi project
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
@Import(RpiSensorConfig.class)
@ComponentScan("cz.pojd.rpi")
public class RpiConfig {

    /**
     * Detects whether this is a new RasPI or not (drives further logic in this project)
     * 
     * @return true if so, false otherwise
     */
    @Bean
    public boolean newRasPI() {
	return true;
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
    public Runtime runtime() {
	return Runtime.getRuntime();
    }

    @Bean
    public RuntimeExecutor runtimeExecutor() {
	return new RuntimeExecutorImpl();
    }
}

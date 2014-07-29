package cz.pojd.homeautomation.hawa.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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

}

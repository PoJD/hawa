package cz.pojd.homeautomation.hawa.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cz.pojd.homeautomation.model.spring.ModelConfig;
import cz.pojd.rpi.spring.RpiConfig;
import cz.pojd.security.spring.SecurityConfig;

/**
 * Main configuration for spring
 * 
 * @author Lubos Housa
 * @since Jul 23, 2014 2:34:22 AM
 */
@Configuration
@Import({ ModelConfig.class, RpiConfig.class, SecurityConfig.class })
public class HawaConfig {
}

package cz.pojd.homeautomation.model.lights;

/**
 * Motion sensor light trigger enables setting up motion sensor for controlling lights
 *
 * @author Lubos Housa
 * @since Nov 3, 2014 1:04:28 AM
 */
public interface MotionSensorLightTrigger {
    void setup(MotionSensorLightDetails details);
}

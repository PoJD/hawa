package cz.pojd.security.event;

/**
 * Type of SecurityEvent
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 7:52:25 PM
 */
public enum Type {
    sensorMotionDetected("Detected motion (sensor)"), cameraMotionDetected("Detected motion (camera)"),
    doorOpened("Door opened"), garageOpened("Garage opened"), windowOpened("Window opened"),
    highTemperature("High temperature");

    private Type(String name) {
	this.name = name;
    }

    private final String name;

    /**
     * Returns human readable name of this type
     * 
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * Returns unique identifier of this type
     * 
     * @return
     */
    public String getId() {
	return name();
    }
}

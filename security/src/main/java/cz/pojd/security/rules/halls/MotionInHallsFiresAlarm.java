package cz.pojd.security.rules.halls;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Source;
import cz.pojd.security.event.Type;
import cz.pojd.security.rules.Rule;

/**
 * This rule fires an alarm any time a security event with motion detected in halls is processed
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 12:10:45 AM
 */
public class MotionInHallsFiresAlarm implements Rule {

    @Override
    public boolean isSecurityBreach(SecurityEvent event) {
	return (Source.HALL_DOWN == event.getSource() || Source.HALL_UP == event.getSource()) && Type.sensorMotionDetected == event.getType();
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return SecurityMode.EMPTY_HOUSE == securityMode;
    }

    @Override
    public String getDescription() {
	return "Motion detected inside any hall fires alarm if the house is empty";
    }
}

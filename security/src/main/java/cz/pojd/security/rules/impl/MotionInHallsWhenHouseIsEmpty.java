package cz.pojd.security.rules.impl;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Source;
import cz.pojd.security.event.Type;

public class MotionInHallsWhenHouseIsEmpty extends AbstractRule {

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
	return "Motion detected inside any hall (empty house)";
    }
}

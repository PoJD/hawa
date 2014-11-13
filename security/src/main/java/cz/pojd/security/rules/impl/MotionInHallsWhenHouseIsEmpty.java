package cz.pojd.security.rules.impl;

import cz.pojd.homeautomation.model.spring.RoomSpecification;
import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;
import cz.pojd.security.rules.SecurityBreach;

public class MotionInHallsWhenHouseIsEmpty extends AbstractRule {

    @Override
    public SecurityBreach isSecurityBreach(SecurityEvent event) {
	return SecurityBreach.valueOf((RoomSpecification.HALL_DOWN == event.getSource() || RoomSpecification.HALL_UP == event.getSource())
		&& Type.sensorMotionDetected == event.getType());
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

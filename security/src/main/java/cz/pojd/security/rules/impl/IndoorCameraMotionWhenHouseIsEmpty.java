package cz.pojd.security.rules.impl;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;

public class IndoorCameraMotionWhenHouseIsEmpty extends AbstractRule {

    @Override
    public boolean isSecurityBreach(SecurityEvent event) {
	return !event.getSource().isOutdoor() && Type.cameraMotionDetected == event.getType();
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return SecurityMode.EMPTY_HOUSE == securityMode;
    }

    @Override
    public String getDescription() {
	return "Motion detected by camera inside the house (empty house)";
    }
}

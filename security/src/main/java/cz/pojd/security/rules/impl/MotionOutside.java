package cz.pojd.security.rules.impl;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.SecuritySource;
import cz.pojd.security.event.Type;
import cz.pojd.security.rules.SecurityBreach;

public class MotionOutside extends AbstractRule {

    @Override
    public SecurityBreach isSecurityBreach(SecurityEvent event) {
	return (SecuritySource.OUTDOOR == event.getSource() && (Type.sensorMotionDetected == event.getType() || Type.cameraMotionDetected == event
		.getType())) ? SecurityBreach.DELAYED : SecurityBreach.NONE;
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return SecurityMode.OFF != securityMode;
    }

    @Override
    public String getDescription() {
	return "Motion detected outside the house - security still on " + SecurityBreach.DELAY_MINUTES + " minutes later (empty or full house)";
    }
}

package cz.pojd.security.rules.impl;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.SecuritySource;
import cz.pojd.security.event.Type;

public class MotionOutside extends AbstractRule {

    @Override
    public boolean isSecurityBreach(SecurityEvent event) {
	// TODO this should fire a later event, e.g. say 10mins later is the alarm is still off (e.g. somehow re-throw the same security event and let
	// it process?)
	return SecuritySource.OUTDOOR == event.getSource() && (Type.sensorMotionDetected == event.getType() || Type.cameraMotionDetected == event.getType());
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return SecurityMode.OFF != securityMode;
    }

    @Override
    public String getDescription() {
	return "Motion detected outside the house (empty or full house)";
    }
}

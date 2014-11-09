package cz.pojd.security.rules.impl;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;

public class DoorOrGarageOpened extends AbstractRule {

    @Override
    public boolean isSecurityBreach(SecurityEvent event) {
	return Type.doorOpened == event.getType() || Type.garageOpened == event.getType();
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return SecurityMode.OFF != securityMode;
    }

    @Override
    public String getDescription() {
	return "Door or garage opened (empty or full house)";
    }
}

package cz.pojd.security.rules.impl;

import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;
import cz.pojd.security.rules.SecurityBreach;

public class HighTemperature extends AbstractRule {

    @Override
    public SecurityBreach isSecurityBreach(SecurityEvent event) {
	return SecurityBreach.valueOf(Type.highTemperature == event.getType());
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return true;
    }

    @Override
    public String getDescription() {
	return "Temperature is too high";
    }
}

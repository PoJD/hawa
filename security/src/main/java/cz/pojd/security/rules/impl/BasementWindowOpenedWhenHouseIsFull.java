package cz.pojd.security.rules.impl;

import cz.pojd.homeautomation.model.Floor;
import cz.pojd.security.controller.SecurityMode;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Type;
import cz.pojd.security.rules.SecurityBreach;

public class BasementWindowOpenedWhenHouseIsFull extends AbstractRule {

    @Override
    public SecurityBreach isSecurityBreach(SecurityEvent event) {
	return SecurityBreach.valueOf(Type.windowOpened == event.getType() && Floor.Basement == event.getSource().getFloor());
    }

    @Override
    public boolean isApplicable(SecurityMode securityMode) {
	return SecurityMode.FULL_HOUSE == securityMode;
    }

    @Override
    public String getDescription() {
	return "A window in basement opened (full house)";
    }
}

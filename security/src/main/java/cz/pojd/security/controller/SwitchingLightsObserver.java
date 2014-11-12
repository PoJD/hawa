package cz.pojd.security.controller;

import javax.inject.Inject;

import cz.pojd.homeautomation.model.rooms.RoomsDAO;
import cz.pojd.rpi.controllers.Observer;

/**
 * Special observer for security controller simply switching all lights in rooms DAO anytime the security mode is set to empty house (e.g. anytime
 * someone is last one to leave the house)
 *
 * @author Lubos Housa
 * @since Nov 12, 2014 5:55:49 PM
 */
public class SwitchingLightsObserver implements Observer<Controller, SecurityMode> {

    private final RoomsDAO roomsDAO;

    @Inject
    public SwitchingLightsObserver(RoomsDAO roomsDAO) {
	this.roomsDAO = roomsDAO;
    }

    @Override
    public void update(Controller source, SecurityMode newValue) {
	if (SecurityMode.EMPTY_HOUSE == newValue) {
	    roomsDAO.switchOffAllLights();
	}
    }
}

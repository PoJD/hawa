package cz.pojd.rpi.controls;

import cz.pojd.rpi.State;

public abstract class BaseControl extends ControllableBase implements Control {

    @Override
    public void disable() {
	switchOff();
	super.disable();
    }

    @Override
    public boolean isSwitchable() {
	return true;
    }

    @Override
    public boolean setSwitchedOn(boolean switchedOn) {
	if (switchedOn) {
	    return switchOn();
	} else {
	    return switchOff();
	}
    }

    @Override
    public State getState() {
	return State.newBuilder().initiated(isInitiated()).enabled(isEnabled()).switchedOn(isSwitchedOn()).switchable(isSwitchable()).build();
    }

    @Override
    public void resetFrom(State state) {
	if (state != null) {
	    setEnabled(state.isEnabled());
	    setSwitchedOn(state.isSwitchedOn());
	}
    }
}

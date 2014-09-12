package cz.pojd.rpi.controls;

import cz.pojd.rpi.State;

public abstract class BaseControl extends ControllableBase implements Control {

    @Override
    public void disable() {
	switchOff();
	super.disable();
    }

    @Override
    public State getState() {
	return State.newBuilder().initiated(isInitiated()).enabled(isEnabled()).on(isSwitchedOn()).switchable(true).build();
    }
}

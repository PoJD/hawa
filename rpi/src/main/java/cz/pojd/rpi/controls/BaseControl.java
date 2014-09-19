package cz.pojd.rpi.controls;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.rpi.State;

public abstract class BaseControl extends ControllableBase implements Control {
    private static final Log LOG = LogFactory.getLog(BaseControl.class);

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
	State result = State.newBuilder().initiated(isInitiated()).enabled(isEnabled()).switchedOn(isSwitchedOn()).switchable(isSwitchable()).build();
	if (LOG.isDebugEnabled()) {
	    LOG.debug(this + " getState: " + result);
	}
	return result;
    }

    @Override
    public void resetFrom(State state) {
	if (state != null) {
	    setEnabled(state.isEnabled());
	    setSwitchedOn(state.isSwitchedOn());
	}
    }
}

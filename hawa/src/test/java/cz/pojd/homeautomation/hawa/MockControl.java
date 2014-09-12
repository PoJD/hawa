package cz.pojd.homeautomation.hawa;

import cz.pojd.rpi.controls.BaseControl;

public class MockControl extends BaseControl {
    private boolean on;

    public boolean toggleSwitch() {
	return on = !on;
    }

    public boolean switchOn() {
	on = true;
	return true;
    }

    public boolean switchOff() {
	on = false;
	return true;
    }

    public boolean isSwitchedOn() {
	return on;
    }

    @Override
    public boolean isInitiated() {
	return true;
    }
}

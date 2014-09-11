package cz.pojd.rpi.controls;

import cz.pojd.rpi.sensors.SensorState;

public abstract class BaseControl extends ControllableBase implements Control {

    @Override
    public void disable() {
	switchOff();
	super.disable();
    }

    @Override
    public SensorState asSensorState() {
	return SensorState.newBuilder().enabled(isEnabled()).on(isSwitchedOn()).build();
    }
}

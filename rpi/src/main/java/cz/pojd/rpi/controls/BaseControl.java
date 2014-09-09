package cz.pojd.rpi.controls;

public abstract class BaseControl extends ControllableBase implements Control {

    @Override
    public void disable() {
	switchOff();
	super.disable();
    }
}

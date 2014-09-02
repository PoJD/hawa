package cz.pojd.rpi.controls;

public abstract class BaseControl implements Control {
    private boolean enabled = true;

    @Override
    public void disable() {
	switchOff();
	enabled = false;
    }

    @Override
    public void enable() {
	enabled = true;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }
}

package cz.pojd.rpi.controls;

public abstract class ControllableBase implements Controllable {
    private boolean enabled = true;

    @Override
    public void disable() {
	enabled = false;
    }

    @Override
    public void enable() {
	enabled = true;
    }

    @Override
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }
}

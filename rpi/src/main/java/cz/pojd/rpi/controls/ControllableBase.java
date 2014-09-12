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
	// on purpose do not set the enabled flag here directly - allow descendants override enable or disable as needed
	if (enabled) {
	    enable();
	} else {
	    disable();
	}
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }
}

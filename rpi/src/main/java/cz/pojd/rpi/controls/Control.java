package cz.pojd.rpi.controls;

import cz.pojd.rpi.State;

/**
 * Generic control in the application - used to control various devices through various means
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:54:45 AM
 */
public interface Control extends Controllable {

    /**
     * Toggle the switch (switch on if off before or switch off if on before)
     * 
     * @return true if toggle was OK, false otherwise
     */
    public boolean toggleSwitch();

    /**
     * Switch on this Control
     * 
     * @return true if switch on was OK, false otherwise
     */
    public boolean switchOn();

    /**
     * Switch off this Control
     * 
     * @return true if switch on was OK, false otherwise
     */
    public boolean switchOff();

    /**
     * Sets whether this control is switched on or not
     * 
     * @param switchedOn
     *            new value of switched on
     * @return true if switched on, false if switched off
     */
    public boolean setSwitchedOn(boolean switchedOn);

    /**
     * Detects whether this control is currently switched on or not.
     * 
     * @return true if so, false otherwise
     */
    public boolean isSwitchedOn();

    /**
     * Returns this control's state - e.g. whether it is enabled and switched on or not
     * 
     * @return wrapper for this control state
     */
    public State getState();

    /**
     * Detects whether this control is properly initiated or not
     * 
     * @return true if so, false otherwise
     */
    public boolean isInitiated();
}

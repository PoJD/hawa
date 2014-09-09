package cz.pojd.rpi.controls;

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
     * Detects whether this control is currently switched on or not.
     * 
     * @return true if so, false otherwise
     */
    public boolean isSwitchedOn();
}

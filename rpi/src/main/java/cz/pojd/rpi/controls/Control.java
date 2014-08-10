package cz.pojd.rpi.controls;

/**
 * Generic control in the application - used to control various devices through various means
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:54:45 AM
 */
public interface Control {

    /**
     * Toggle the switch (switch on if off before or switch off if on before)
     */
    public void toggleSwitch();

    public void switchOn();

    public void switchOff();

    /**
     * Disable this control
     */
    public void disable();

    /**
     * Enable this control
     */
    public void enable();
}

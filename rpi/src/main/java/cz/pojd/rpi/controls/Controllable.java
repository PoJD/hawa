package cz.pojd.rpi.controls;

/**
 * Controllable allows enabling and disabling
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 10:54:45 AM
 */
public interface Controllable {
    
    /**
     * Disable this item
     */
    public void disable();

    /**
     * Enable this item
     */
    public void enable();
    
    /**
     * Sets whether this controllable is enabled or not
     * @param enabled true if so, false otherwise
     */
    public void setEnabled(boolean enabled);

    /**
     * Detects whether this item is enabled or not
     * 
     * @return true if so, false otherwise. Note that by default all items are enabled
     */
    public boolean isEnabled();
}

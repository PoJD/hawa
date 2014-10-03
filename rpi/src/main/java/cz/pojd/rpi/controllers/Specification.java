package cz.pojd.rpi.controllers;

/**
 * Generic specification instance, used by the observers
 *
 * @author Lubos Housa
 * @since Oct 3, 2014 11:19:44 PM
 */
public interface Specification {

    /**
     * Detects whether this specification is satisfied.
     * 
     * @return true if so, false otherwise
     */
    public boolean isSatisfied();
}

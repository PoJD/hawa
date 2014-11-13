package cz.pojd.security.rules;

/**
 * Security breach enumeration
 *
 * @author Lubos Housa
 * @since Nov 13, 2014 9:41:47 PM
 */
public enum SecurityBreach {

    /** No Security Breach */
    NONE,

    /** Security Breach Now - raised immediately */
    NOW,

    /**
     * Delayed security breach - would result in security breach if still evaluated so later (security controller can re-evaluate rules later when
     * this is the evaluation result. Marked as minor security breach
     */
    DELAYED;

    /**
     * How many minutes should a delayed security breach be delayed before being fired (assuming the rule is still active)
     */
    public static final int DELAY_MINUTES = 10;

    /**
     * Parses the given boolean as SecurityBreach value
     * 
     * @param value
     *            boolean to parse (true for security breach, false for no security breach)
     * @return SecurityBreach representation of the in-passed boolean
     */
    public static SecurityBreach valueOf(boolean value) {
	return value ? NOW : NONE;
    }
}

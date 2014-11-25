package cz.pojd.security.event;

/**
 * Translator to be translate security events to various sources
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 5:36:56 PM
 */
public interface SecurityEventTranslator<T> {

    /**
     * Translate the security event to the specified type
     * 
     * @param securityEvent
     *            security event to translate
     * @return the required type
     */
    T translate(SecurityEvent securityEvents);
}

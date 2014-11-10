package cz.pojd.security.event;

import java.util.Collection;

import cz.pojd.homeautomation.model.web.CalendarEvent;

/**
 * Translator to be able to create calendar events
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 5:36:56 PM
 */
public interface CalendarEventTranslator {

    /**
     * Translate the collection of security events to calendar events
     * 
     * @param securityEvents
     *            security events to translate
     * @return collection of calendar events of the same size
     */
    Collection<CalendarEvent> translate(Collection<SecurityEvent> securityEvents);
}

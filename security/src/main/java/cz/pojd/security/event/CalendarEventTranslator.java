package cz.pojd.security.event;

import cz.pojd.homeautomation.model.web.CalendarEvent;

/**
 * Simple interface to avoid spring injecting templates into service since that does not work in Jersey
 *
 * @author Lubos Housa
 * @since Nov 26, 2014 12:19:44 AM
 */
public interface CalendarEventTranslator extends SecurityEventTranslator<CalendarEvent> {
}

package cz.pojd.security.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.security.event.SecurityEvent;

/**
 * Email sender is simply sending email when a security breach is detected. It fetches the file if present on the event and sends that as an
 * attachement to the email
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 12:42:42 AM
 */
public class EmailSender implements SecurityHandler {
    private static final Log LOG = LogFactory.getLog(EmailSender.class);

    @Override
    public void handleSecurityBreach(SecurityEvent event) {
	LOG.info("Sending out email with the detail of the security event");
	// TODO implement
    }
}

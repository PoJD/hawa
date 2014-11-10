package cz.pojd.security.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.SecurityEventDAO;

/**
 * Special security handler simply delegating to the security event DAO to store this security event into a storage
 *
 * @author Lubos Housa
 * @since Nov 10, 2014 2:38:19 PM
 */
public class SecurityEventStorer implements SecurityHandler {
    private static final Log LOG = LogFactory.getLog(SecurityEventStorer.class);

    private final SecurityEventDAO securityEventDAO;

    public SecurityEventStorer(SecurityEventDAO securityEventDAO) {
	this.securityEventDAO = securityEventDAO;
    }

    @Override
    public void handleSecurityBreach(SecurityEvent event) {
	LOG.info("Storing security event " + event + " using DAO.");
	securityEventDAO.save(event);
    }
}

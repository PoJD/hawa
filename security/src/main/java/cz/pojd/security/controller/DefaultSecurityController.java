package cz.pojd.security.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.pojd.security.event.SecurityEvent;

public class DefaultSecurityController implements Controller {
    private static final Log LOG = LogFactory.getLog(DefaultSecurityController.class);

    @Override
    public void handle(SecurityEvent securityEvent) {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Security event fired: " + securityEvent);
	}
	
	// TODO calendar and maybe email? Some rules here? When to call?
    }

}

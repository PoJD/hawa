package cz.pojd.homeautomation.hawa.refresh;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;

/**
 * Generic refreshable DAO.
 * 
 * @author Lubos Housa
 * @since Aug 20, 2014 10:10:33 PM
 */
public abstract class RefreshableDAO implements Refreshable {

    private static final Log LOG = LogFactory.getLog(RefreshableDAO.class);

    @Inject
    protected RefreshableDAO(Refresher refresher) {
	refresher.register(this);
    }

    @Override
    public void refresh(LocalDateTime dateTime) {
	LOG.info("Refresh triggered in " + this.getClass().getSimpleName() + " - detecting current state and saving it...");
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Detecting current state...");
	}
	detectState();
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Current state detected. Saving...");
	}
	saveState(dateTime);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Current state saved.");
	}
	LOG.info("Refresh in " + this.getClass().getSimpleName() + " finished.");
    }

    /**
     * Saving current state previously detected.
     */
    protected abstract void saveState(LocalDateTime dateTime);

    /**
     * Detect current state and save anything needed to be saved on this instance (e.g. data to be returned later)
     */
    protected abstract void detectState();
}

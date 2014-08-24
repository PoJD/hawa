package cz.pojd.homeautomation.hawa.refresh;

import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Generic refreshable DAO.
 * 
 * @author Lubos Housa
 * @since Aug 20, 2014 10:10:33 PM
 */
public abstract class RefreshableDAO implements Refreshable {

    private static final Log LOG = LogFactory.getLog(RefreshableDAO.class);
    private JdbcTemplate jdbcTemplate;

    @Inject
    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Inject
    public void setRefresher(Refresher refresher) {
	refresher.register(this);
    }

    public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    /**
     * Sets the JDBC template of this DAO. Either use this one directly or the setDataSource instead
     * 
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void refresh(LocalDateTime dateTime) {
	LOG.info("Refresh triggered in " + this.getClass().getSimpleName() + " - detecting current state and saving it...");
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Detecting current state...");
	}
	try {
	    detectState();
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Current state detected. Saving...");
	    }
	} catch (Exception e) {
	    LOG.error("Error detecting state at " + dateTime + ": ", e);
	}
	try {
	    saveState(dateTime.toDate());
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Current state saved.");
	    }
	} catch (Exception e) {
	    LOG.error("Error saving state at " + dateTime + ": ", e);
	}
	LOG.info("Refresh in " + this.getClass().getSimpleName() + " finished.");
    }

    /**
     * Saving current state previously detected.
     */
    protected abstract void saveState(Date date);

    /**
     * Detect current state and save anything needed to be saved on this instance (e.g. data to be returned later)
     */
    protected abstract void detectState();
}

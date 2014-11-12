package cz.pojd.homeautomation.model.dao;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Generic DAO implementaiton (aka JdbcDaoSupport from Spring, but with autowired fields)
 * 
 * @author Lubos Housa
 * @since Aug 20, 2014 10:10:33 PM
 */
public abstract class DAOImpl implements DAO {

    private static final Log LOG = LogFactory.getLog(DAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Inject
    public final void setDataSource(DataSource dataSource) {
	if (LOG.isInfoEnabled()) {
	    LOG.info("Using datasource: " + dataSource + " and JDBC Driver: '" + jdbcDriver + "' and jdbc URL: '" + jdbcUrl + "'.");
	}
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public final JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    /**
     * Sets the JDBC template of this DAO. Either use this one directly or the setDataSource instead
     * 
     * @param jdbcTemplate
     */
    public final void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }
}

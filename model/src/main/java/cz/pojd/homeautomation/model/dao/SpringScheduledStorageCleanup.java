package cz.pojd.homeautomation.model.dao;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Storage cleanup implementation leveraging spring scheduling using @Scheduled annotation
 *
 * @author Lubos Housa
 * @since Nov 12, 2014 1:02:15 PM
 */
public class SpringScheduledStorageCleanup extends DAOImpl implements StorageCleanup {
    private static final Log LOG = LogFactory.getLog(SpringScheduledStorageCleanup.class);

    private final Set<String> tableNames = new TreeSet<>();
    private final Pattern pattern = Pattern.compile("%TABLENAME%"); // should match the variable inside sql property below

    @Value("${sql.storageCleanup.statement}")
    private String deleteSql;

    public String getDeleteSql() {
	return deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
	this.deleteSql = deleteSql;
    }

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    @Override
    public synchronized void cleanup() {
	LOG.info("Cleanup triggered in SpringScheduledStorageCleanup - cleaning up all registered tables : " + tableNames);

	for (String tableName : tableNames) {
	    Matcher matcher = pattern.matcher(getDeleteSql());
	    String sql = matcher.replaceFirst(tableName);
	    if (LOG.isDebugEnabled()) {
		LOG.debug("Running sql \"" + sql + "\" for " + PRESERVE_MONTHS_BACK + " months back.");
	    }
	    getJdbcTemplate().update(sql, PRESERVE_MONTHS_BACK);
	}

	LOG.info("Cleanup finished.");
    }

    @Override
    public synchronized void registerTable(String tableName) {
	tableNames.add(tableName);
    }
}

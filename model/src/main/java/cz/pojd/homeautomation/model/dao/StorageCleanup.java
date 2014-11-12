package cz.pojd.homeautomation.model.dao;

/**
 * Storage cleanup is a service that periodically cleans up the storage for old and unused data.
 *
 * @author Lubos Housa
 * @since Nov 12, 2014 12:51:02 PM
 */
public interface StorageCleanup {

    /**
     * How many months back should this cleanup preserve. Anything older than this will be at some point deleted.
     */
    int PRESERVE_MONTHS_BACK = 2;

    /**
     * Register a table to be checked and cleaned up when needed. THe requirement is that this table has a column 'at' which is a java.sql.Timestamp
     * type and represents the age of that particular DB entry.
     * 
     * @param tableName
     *            table name to cleanup
     */
    void registerTable(String tableName);

    /**
     * Manually perform the cleanup. Note that the implementation guarantees it will have it our means of schedulling of this method invocation.
     * CLients are however allowed to trigger this method manually too.
     */
    void cleanup();
}

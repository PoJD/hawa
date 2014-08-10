package cz.pojd.homeautomation.hawa.outdoor;

/**
 * DAO for outdoor
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 4:07:08 PM
 */
public interface OutdoorDAO {

    /**
     * Get the outdoor state
     * 
     * @return outdoor POJO representing outdoor state
     */
    Outdoor get();

    /**
     * Save the Outdoor
     * 
     * @param outdoor
     *            outdoor to save
     */
    void save(Outdoor outdoor);
}

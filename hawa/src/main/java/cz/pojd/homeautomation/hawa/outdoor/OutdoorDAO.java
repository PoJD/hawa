package cz.pojd.homeautomation.hawa.outdoor;

import cz.pojd.homeautomation.hawa.refresh.Refreshable;

/**
 * DAO for outdoor
 * 
 * @author Lubos Housa
 * @since Aug 10, 2014 4:07:08 PM
 */
public interface OutdoorDAO extends Refreshable {

    /**
     * Get the outdoor detail
     * 
     * @return outdoor POJO representing outdoor state
     */
    OutdoorDetail get();

    /**
     * Save the Outdoor
     * 
     * @param outdoorDetail
     *            outdoor to save
     */
    void save(OutdoorDetail outdoorDetail);
}

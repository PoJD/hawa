package cz.pojd.homeautomation.model.outdoor;

import cz.pojd.homeautomation.model.refresh.Refreshable;

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
     * Get the outdoor detail including the outdoor history (evolution of the sensors)
     * 
     * @return outdoor POJO representing outdoor state and history
     */
    OutdoorDetail getWithHistory();

    /**
     * Save the Outdoor
     * 
     * @param outdoorDetail
     *            outdoor to save
     */
    void save(OutdoorDetail outdoorDetail);
}

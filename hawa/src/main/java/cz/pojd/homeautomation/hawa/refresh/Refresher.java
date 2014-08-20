package cz.pojd.homeautomation.hawa.refresh;

/**
 * Refresher is the main point of refreshing data in the application
 * 
 * @author Lubos Housa
 * @since Aug 19, 2014 5:38:44 PM
 */
public interface Refresher {

    /**
     * Run the refresh logic
     */
    public void refresh();

    /**
     * Register the in passed instance to be notified about refresh triggers from this refresher. Note that this method is not thread save, so it is
     * adviced to invoke this method in one thread only and only upon startup. Invoking this method at runtime may yield unexpected results
     * 
     * @param refreshable
     *            refreshable instance to be notified (refreshed) when this refresher triggers that logic
     */
    public void register(Refreshable refreshable);

    /**
     * Gets last update time - when this refresher ran
     * 
     * @return last date/time this instance ran
     */
    public String getLastUpdate();
}

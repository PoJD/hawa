package cz.pojd.security.event;

import cz.pojd.homeautomation.model.Source;

/**
 * Service capable of resolving sources by name
 *
 * @author Lubos Housa
 * @since Dec 9, 2014 10:14:33 PM
 */
public interface SourceResolver {

    /**
     * Resolve the source by the given name
     * 
     * @param name
     *            name of the source to lookup
     * @return found source or Camera.UNKNOWN if no valid source found
     */
    Source resolve(String name);
}

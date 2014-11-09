package cz.pojd.security.rules;

import java.util.List;

/**
 * DAO for Rules
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 11:03:41 AM
 */
public interface RulesDao {

    /**
     * Get all rules
     * 
     * @return list of rules
     */
    public List<Rule> getAll();
}

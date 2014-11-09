package cz.pojd.security.rules;

import java.util.Collection;

/**
 * DAO for Rules
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 11:03:41 AM
 */
public interface RulesDAO {

    /**
     * Get all rules
     * 
     * @return list of rules
     */
    Collection<Rule> queryAllRules();

    /**
     * Get the details of all rules
     * 
     * @return list of all rules' details
     */
    Collection<RuleDetail> queryAllRuleDetails();

    /**
     * Save the rule detail
     * 
     * @param ruleDetail
     *            rule detail to save
     */
    void save(RuleDetail ruleDetail);
}

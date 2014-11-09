package cz.pojd.security.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RulesDAOImpl implements RulesDAO {
    private static final Log LOG = LogFactory.getLog(RulesDAOImpl.class);

    private Map<Integer, Rule> rules;

    @Inject
    public RulesDAOImpl(Rule... rules) {
	Map<Integer, Rule> map = new LinkedHashMap<>();
	if (rules != null) {
	    for (Rule rule : rules) {
		map.put(rule.getId(), rule);
	    }
	}
	this.rules = Collections.unmodifiableMap(map);
    }

    @Override
    public Collection<Rule> queryAllRules() {
	return rules.values();
    }

    @Override
    public Collection<RuleDetail> queryAllRuleDetails() {
	List<RuleDetail> result = new ArrayList<>();
	for (Rule rule : rules.values()) {
	    result.add(new RuleDetail(rule));
	}
	return result;
    }

    @Override
    public void save(RuleDetail ruleDetail) {
	LOG.info("About to save rule detail: " + ruleDetail);
	Rule rule = rules.get(ruleDetail.getId());
	if (rule != null) {
	    rule.updateFrom(ruleDetail);
	    LOG.info("Rule detail saved.");
	} else {
	    LOG.warn("Unable to find rule by id: " + ruleDetail.getId() + ". Ignoring the call to save().");
	}
    }
}

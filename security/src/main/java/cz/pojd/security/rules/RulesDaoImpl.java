package cz.pojd.security.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class RulesDaoImpl implements RulesDao {

    private List<Rule> rules;

    @Inject
    public RulesDaoImpl(Rule... rules) {
	List<Rule> list = new ArrayList<>();
	if (rules != null) {
	    for (Rule rule : rules) {
		list.add(rule);
	    }
	}
	this.rules = Collections.unmodifiableList(list);
    }

    @Override
    public List<Rule> getAll() {
	return rules;
    }
}

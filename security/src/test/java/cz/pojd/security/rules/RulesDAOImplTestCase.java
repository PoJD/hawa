package cz.pojd.security.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

public class RulesDAOImplTestCase {

    private RulesDAOImpl dao;
    @Mocked
    private Rule rule;

    @Before
    public void setup() {
	dao = new RulesDAOImpl(rule);
    }

    @Test
    public void testQueryAllRules() {
	Collection<Rule> rules = dao.queryRules();

	Iterator<Rule> it = rules.iterator();
	assertTrue(it.hasNext());
	assertEquals(rule, it.next());
	assertFalse(it.hasNext());
    }

    @Test
    public void testQueryAllRuleDetails() {
	Collection<RuleDetail> ruleDetails = dao.query();

	Iterator<RuleDetail> it = ruleDetails.iterator();
	assertTrue(it.hasNext());

	RuleDetail ruleDetail = it.next();
	assertEquals(rule.getId(), ruleDetail.getId());
	assertEquals(rule.getDescription(), ruleDetail.getDescription());
	assertEquals(rule.isEnabled(), ruleDetail.isEnabled());
	assertFalse(it.hasNext());
    }

    @Test
    public void testSave() {
	final RuleDetail ruleDetail = new RuleDetail(rule);
	new NonStrictExpectations() {
	    {
		rule.updateFrom(ruleDetail);
		times = 1;
	    }
	};

	dao.save(ruleDetail);
    }
    

    @Test
    public void testSaveInvalidRuleDetail() {
	final RuleDetail ruleDetail = new RuleDetail();
	ruleDetail.setId(55);
	new NonStrictExpectations() {
	    {
		rule.updateFrom(ruleDetail);
		maxTimes = 0;
	    }
	};

	dao.save(ruleDetail);
    }
}

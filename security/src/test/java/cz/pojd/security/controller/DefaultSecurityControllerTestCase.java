package cz.pojd.security.controller;

import java.util.Arrays;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDao;

public class DefaultSecurityControllerTestCase {

    private DefaultSecurityController controller;
    @Mocked
    private RulesDao rulesDao;
    @Mocked
    private Rule rule, rule2;
    @Mocked
    private SecurityHandler securityHandler;
    @Mocked
    private SecurityEvent securityEvent;

    @Before
    public void setup() {
	new NonStrictExpectations() {
	    {
		rule.isApplicable(SecurityMode.FULL_HOUSE);
		result = true;
		rule.isApplicable(SecurityMode.EMPTY_HOUSE);
		result = true;

		rule2.isApplicable(SecurityMode.FULL_HOUSE);
		result = true;
		rule2.isApplicable(SecurityMode.EMPTY_HOUSE);
		result = false;

		rulesDao.getAll();
		result = Arrays.asList(rule, rule2);
	    }
	};

	controller = new DefaultSecurityController(rulesDao, securityHandler);
    }

    @Test
    public void testHandleFullHouseFiresRuleAndRule2() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		times = 1;
		result = false;

		rule2.isSecurityBreach(securityEvent);
		times = 1;
		result = false;
	    }
	};
	controller.switchMode(SecurityMode.FULL_HOUSE);
	controller.handle(securityEvent);
    }

    @Test
    public void testHandleEmptyHouseFiresRuleButDoesNotFireRule2() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		times = 1;
		result = false;

		rule2.isSecurityBreach(securityEvent);
		maxTimes = 0;
	    }
	};
	controller.switchMode(SecurityMode.EMPTY_HOUSE);
	controller.handle(securityEvent);
    }

    @Test
    public void testHandleRuleFiresBreachTriggersHandler() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		times = 1;
		result = true;

		securityHandler.handleSecurityBreach(securityEvent);
		times = 1;
	    }
	};
	controller.handle(securityEvent);
    }

    @Test
    public void testHandleRuleNoBreachDoesNotTriggerAnyHandler() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		times = 1;
		result = false;

		securityHandler.handleSecurityBreach(securityEvent);
		maxTimes = 0;
	    }
	};
	controller.handle(securityEvent);
    }

    @Test
    public void testSwitchModeOffNoRuleIsInvoked() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		maxTimes = 0;

		rule2.isSecurityBreach(securityEvent);
		maxTimes = 0;
	    }
	};
	controller.switchMode(SecurityMode.OFF);
	controller.handle(securityEvent);
    }

    @Test
    public void testSecurityEventIsDisposedWhenProcessingOKAndModeIsOff() {
	new NonStrictExpectations() {
	    {
		securityEvent.dispose();
		times = 1;
	    }
	};
	controller.switchMode(SecurityMode.OFF);
	controller.handle(securityEvent);
    }

    @Test(expected=RuntimeException.class)
    public void testSecurityEventIsDisposedWhenProcessingThrowsAnError() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		result = new RuntimeException("Unknown error during isSecurityBreach call");

		securityEvent.dispose();
		times = 1;
	    }
	};
	controller.handle(securityEvent);
    }
}

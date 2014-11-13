package cz.pojd.security.controller;

import java.util.Arrays;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import cz.pojd.rpi.controllers.Observer;
import cz.pojd.rpi.system.TimeService;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.handler.SecurityHandler;
import cz.pojd.security.rules.Rule;
import cz.pojd.security.rules.RulesDAO;
import cz.pojd.security.rules.SecurityBreach;

public class DefaultSecurityControllerTestCase {

    private DefaultSecurityController controller;
    @Mocked
    private RulesDAO rulesDAO;
    @Mocked
    private Rule rule, rule2, rule3;
    @Mocked
    private SecurityHandler securityHandler;
    @Mocked
    private SecurityEvent securityEvent, securityEvent2;
    @Mocked
    private TimeService timeService;
    @Mocked
    private Observer<Controller, SecurityMode> observer;

    @Before
    public void setup() {
	new NonStrictExpectations() {
	    {
		rule.isApplicable(SecurityMode.FULL_HOUSE);
		result = true;
		rule.isApplicable(SecurityMode.EMPTY_HOUSE);
		result = true;
		rule.isEnabled();
		result = true;

		rule2.isApplicable(SecurityMode.FULL_HOUSE);
		result = true;
		rule2.isApplicable(SecurityMode.EMPTY_HOUSE);
		result = false;
		rule2.isEnabled();
		result = true;

		rule3.isApplicable(SecurityMode.FULL_HOUSE);
		result = true;
		rule3.isApplicable(SecurityMode.EMPTY_HOUSE);
		result = true;
		rule3.isEnabled();
		result = false;

		rulesDAO.queryRules();
		result = Arrays.asList(rule, rule2, rule3);
	    }
	};

	controller = new DefaultSecurityController(timeService, rulesDAO, securityHandler);
	controller.addObserver(observer);
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
    public void testHandleDoesNotFireDisabledRule() {
	new NonStrictExpectations() {
	    {
		rule3.isSecurityBreach(securityEvent);
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
	controller.switchMode(SecurityMode.FULL_HOUSE);
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
	controller.switchMode(SecurityMode.FULL_HOUSE);
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
    public void testSwitchModeObserverIsNotified() {
	new NonStrictExpectations() {
	    {
		observer.update(controller, SecurityMode.OFF);
	    }
	};
	controller.switchMode(SecurityMode.OFF);
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

    @Test(expected = RuntimeException.class)
    public void testSecurityEventIsDisposedWhenProcessingThrowsAnError() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		result = new RuntimeException("Unknown error during isSecurityBreach call");

		securityEvent.dispose();
		times = 1;
	    }
	};
	controller.switchMode(SecurityMode.FULL_HOUSE);
	controller.handle(securityEvent);
    }

    @Test
    public void testSecurityEventDelayedDosNotCallSecurityHandlersOrDispose() {
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		result = SecurityBreach.DELAYED;
		times = 1;

		securityEvent.dispose();
		maxTimes = 0;

		securityHandler.handleSecurityBreach(withInstanceOf(SecurityEvent.class));
		maxTimes = 0;
	    }
	};
	controller.switchMode(SecurityMode.FULL_HOUSE);
	controller.handle(securityEvent);
    }

    @Test
    public void testSecurityEventDelayedIsCalledLater() {
	final DateTime now = DateTime.now();
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(withInstanceLike(securityEvent));
		result = SecurityBreach.DELAYED;

		securityEvent.getAt();
		result = now;
		securityEvent2.getAt();
		result = now.plusMinutes(4);

		securityEvent.compareTo(securityEvent2);
		result = -1;
		securityEvent2.compareTo(securityEvent);
		result = 1;

		timeService.now();
		returns(now, now.plusMinutes(12));

		securityHandler.handleSecurityBreach(securityEvent);
		times = 1;

		securityEvent.dispose();
		times = 1;

		securityHandler.handleSecurityBreach(securityEvent2);
		maxTimes = 0;

		securityEvent2.dispose();
		maxTimes = 0;
	    }
	};
	controller.switchMode(SecurityMode.FULL_HOUSE);
	controller.handle(securityEvent);
	controller.handle(securityEvent2);

	controller.handleDelayedEvents(); // nothing triggered (now is now)
	controller.handleDelayedEvents(); // only security event triggered (now is now + 12, so only securityEvent is older than 10minutes)
    }

    @Test
    public void testSecurityEventDelayedNotCalledWhenSecurityOffSoonEnough() {
	final DateTime now = DateTime.now();
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(withInstanceLike(securityEvent));
		result = SecurityBreach.DELAYED;

		securityEvent.getAt();
		result = now;
		securityEvent2.getAt();
		result = now.plusMinutes(4);

		securityEvent.compareTo(securityEvent2);
		result = -1;
		securityEvent2.compareTo(securityEvent);
		result = 1;

		timeService.now();
		returns(now, now.plusMinutes(12));

		securityHandler.handleSecurityBreach(withInstanceLike(securityEvent));
		maxTimes = 0;

		securityEvent.dispose();
		times = 1;
		securityEvent2.dispose();
		times = 1;
	    }
	};
	controller.switchMode(SecurityMode.FULL_HOUSE);
	controller.handle(securityEvent);
	controller.handle(securityEvent2);

	controller.handleDelayedEvents(); // nothing triggered (now is now)

	controller.switchMode(SecurityMode.OFF);

	controller.handleDelayedEvents(); // nothing triggered, since nothing should be inside the internal set anymore
    }
    
    @Test
    public void testSecurityEventDelayedRuleDoesNotFireAgainIsDropped() {
	final DateTime now = DateTime.now();
	new NonStrictExpectations() {
	    {
		rule.isSecurityBreach(securityEvent);
		returns(SecurityBreach.DELAYED, SecurityBreach.NONE);

		securityEvent.getAt();
		result = now;
		times = 1;
		
		timeService.now();
		result = now.plusMinutes(12);
		times = 1;
		
		securityHandler.handleSecurityBreach(withInstanceLike(securityEvent));
		maxTimes = 0;

		securityEvent.dispose();
		times = 1;
	    }
	};
	controller.switchMode(SecurityMode.FULL_HOUSE);
	controller.handle(securityEvent);

	controller.handleDelayedEvents(); // nothing triggered (now is now)
    }
}

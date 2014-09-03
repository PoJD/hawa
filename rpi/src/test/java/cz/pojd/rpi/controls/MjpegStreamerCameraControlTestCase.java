package cz.pojd.rpi.controls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.junit.Before;
import org.junit.Test;

import cz.pojd.rpi.system.RuntimeExecutor;

public class MjpegStreamerCameraControlTestCase {

    @Mocked
    private LocalDateTime localDateTime;
    @Mocked
    private RuntimeExecutor runtimeExecutor;

    private LocalDateTime now;

    private MjpegStreamerCameraControl control;

    @Before
    public void setup() {
	now = new LocalDateTime();
	new NonStrictExpectations() {
	    {
		LocalDateTime.now();
		result = now;

		runtimeExecutor.executeNoReturn(anyString);
		result = true;
	    }
	};
	// 1 minute delay in check
	control = new MjpegStreamerCameraControl("start", "end", 1);
	control.setRuntimeExecutor(runtimeExecutor);
    }

    @Test
    public void testNewInstanceIsNoRunning() {
	assertFalse(control.isRunning());
    }

    @Test
    public void testControlIsRunningAfterStart() {
	control.startOrPing();
	assertTrue(control.isRunning());
    }

    @Test
    public void testLastTriggerSetBeforeStart() {
	assertEquals(now, control.getLastUpdate());
    }

    @Test
    public void testLastTriggerChangedAfterStart() {
	now = new LocalDateTime();

	new NonStrictExpectations() {
	    {
		LocalDateTime.now();
		result = now;
	    }
	};

	control.startOrPing();
	assertEquals(now, control.getLastUpdate());
    }

    @Test
    public void testStartOrPingSetsRunningEvenIfExecutorFails() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.executeNoReturn(anyString);
		result = false;
	    }
	};
	control.startOrPing();
	assertTrue(control.isRunning());
    }

    @Test
    public void testDestroySwitchesOffIfRunning() {
	control.startOrPing();
	control.destroy();
	assertFalse(control.isRunning());
    }

    @Test
    public void testDestroySwitchesOffIfRunningEvenIfCommandFailed() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.executeNoReturn(anyString);
		result = false;
	    }
	};
	control.startOrPing();
	control.destroy();
	assertFalse(control.isRunning());
    }

    @Test
    public void testCheckCameraNotRunningDoesNothing() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.executeNoReturn(anyString);
		times = 0;

		now.minusMinutes(anyInt);
		result = now;

		now.isAfter(withInstanceOf(ReadablePartial.class));
		result = false;
	    }
	};
	control.checkCamera();
	assertFalse(control.isRunning());
    }

    @Test
    public void testCheckCameraRunningTooSoonDoesNothing() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.executeNoReturn(anyString);
		times = 1; // only start or ping should call this

		now.minusMinutes(anyInt);
		result = now;

		now.isAfter(withInstanceOf(ReadablePartial.class));
		result = false;
	    }
	};
	control.startOrPing();
	control.checkCamera();
	assertTrue(control.isRunning());
    }

    @Test
    public void testCheckCameraRunningLateSwitchedOffCamera() {
	new NonStrictExpectations() {
	    {
		runtimeExecutor.executeNoReturn(anyString);
		times = 2; // one start and one switch off

		now.minusMinutes(anyInt);
		result = now;

		now.isAfter(withInstanceOf(ReadablePartial.class));
		result = true;
	    }
	};
	control.startOrPing();
	control.checkCamera();
	assertFalse(control.isRunning());
    }
}

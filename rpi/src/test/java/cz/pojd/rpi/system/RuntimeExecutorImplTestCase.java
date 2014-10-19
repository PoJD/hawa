package cz.pojd.rpi.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

public class RuntimeExecutorImplTestCase {

    private RuntimeExecutorImpl runtimeExecutor;
    private Runtime runtime;

    @Mocked
    private Process process;

    @Mocked
    private InputStreamReader streamReader;
    @Mocked
    private BufferedReader bufferedReader;

    @Before
    public void setup() {
	runtime = Runtime.getRuntime();
	runtimeExecutor = new RuntimeExecutorImpl();
	runtimeExecutor.setRuntime(runtime);

	new MockUp<Runtime>() {
	    @Mock
	    public Process exec(String[] str) {
		assertEquals(3, str.length);
		assertEquals("Some command", str[2]);
		return process;
	    }

	    @Mock
	    public int availableProcessors() {
		return 55;
	    }
	};
	new NonStrictExpectations() {
	    {
		new BufferedReader(withInstanceOf(InputStreamReader.class));
		result = bufferedReader;
	    }
	};
    }

    @Test
    public void testGetCpuCount() {
	new Expectations() {
	    {
		runtime.availableProcessors();
	    }
	};
	assertEquals(55, runtimeExecutor.getCpuCount());
    }

    @Test
    public void testExecuteOneLineOK() throws IOException, InterruptedException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		// error stream first, then 1 line OK and null again to close it off
		returns(null, "12345", null);
	    }
	};
	List<Double> result = runtimeExecutor.executeDouble("Some command");
	assertNotNull(result);
	Iterator<Double> it = result.iterator();
	assertTrue(it.hasNext());
	Double value = it.next();
	assertEquals(12345., value.doubleValue(), 0.001);
	assertFalse(it.hasNext());
    }

    @Test
    public void testExecuteMoreLinesOK() throws IOException, InterruptedException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		// error stream first, then 1 line OK and null again to close it off
		returns(null, "1", "2", "3.23", null);
	    }
	};
	List<Double> result = runtimeExecutor.executeDouble("Some command");
	assertNotNull(result);
	Iterator<Double> it = result.iterator();
	assertTrue(it.hasNext());
	assertEquals(1., it.next().doubleValue(), 0.001);
	assertTrue(it.hasNext());
	assertEquals(2., it.next().doubleValue(), 0.001);
	assertTrue(it.hasNext());
	assertEquals(3.23, it.next().doubleValue(), 0.001);
	assertFalse(it.hasNext());
    }

    @Test
    public void testExecuteError() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns("Some error ocurred", null, null);
	    }
	};

	// error returned should force not to fail, but return empty list instead
	List<Double> result = runtimeExecutor.executeDouble("Some command");
	assertTrue(result.isEmpty());
    }
}

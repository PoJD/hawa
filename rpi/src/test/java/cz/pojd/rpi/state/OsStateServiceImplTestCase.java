package cz.pojd.rpi.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import cz.pojd.rpi.state.PropertyValue.Type;
import cz.pojd.rpi.system.RuntimeExecutorImpl;

public class OsStateServiceImplTestCase {

    private static final List<String> fileSystems = Arrays.asList(new String[] { "/dev/sda", "/dev/sdb" });
    private OsStateServiceImpl service;

    @Mocked
    private Process process;

    @Mocked
    private InputStreamReader streamReader;
    @Mocked
    private BufferedReader bufferedReader;

    @Before
    public void setup() {
	service = new OsStateServiceImpl();
	RuntimeExecutorImpl runtimeExecutor = new RuntimeExecutorImpl();
	runtimeExecutor.setRuntime(Runtime.getRuntime());
	service.setRuntimeExecutor(runtimeExecutor);
	service.setFileSystemsNames(fileSystems);

	new MockUp<Runtime>() {
	    @Mock
	    public Process exec(String[] str) {
		return process;
	    }

	    @Mock
	    public int availableProcessors() {
		return 1;
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
    public void testGetFileSystemsOK() throws IOException, InterruptedException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		// error stream first, then two lines for normal output stream - all twice since we have 2 file systems
		returns(null, "1048576", "2097152", null, null, "0", "2097152", null);
	    }
	};

	Iterable<PropertyValue> result = service.getFileSystems();
	assertNotNull(result);
	Iterator<PropertyValue> it = result.iterator();
	assertTrue(it.hasNext());
	PropertyValue value = it.next();
	assertEquals(50, value.getPercentage());
	assertEquals("/dev/sda", value.getName());
	assertEquals("1M/2M", value.getTextValue());
	assertEquals(Type.os, value.getType());

	assertTrue(it.hasNext());
	value = it.next();
	assertEquals(100, value.getPercentage());
	assertEquals("/dev/sdb", value.getName());
	assertEquals("2M/2M", value.getTextValue());
	assertEquals(Type.os, value.getType());
    }

    @Test
    public void testGetFileSystemsError() throws IOException, InterruptedException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		// error stream twice - that's all the service reads in the end..
		returns("Some error ocurred", null, "Another error ocurred", null);
	    }
	};

	Iterable<PropertyValue> result = service.getFileSystems();
	assertNotNull(result);
	assertFalse(result.iterator().hasNext());
    }

    @Test
    public void testGetFileSystemsErrorAndOK() throws IOException, InterruptedException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		// error stream twice - that's all the service reads in the end..
		returns("Some error ocurred", null, null, "1048576", "2097152", null);
	    }
	};

	Iterable<PropertyValue> result = service.getFileSystems();
	assertNotNull(result);

	Iterator<PropertyValue> it = result.iterator();
	assertTrue(it.hasNext());
	PropertyValue value = it.next();
	assertEquals("/dev/sdb", value.getName());
    }

    @Test
    public void testGetRamOK() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns(null, "1048576", "2097152", null);
	    }
	};

	PropertyValue result = service.getRam();
	assertNotNull(result);
	assertEquals(50, result.getPercentage());
	assertTrue(result.getName().contains("RAM"));
	assertEquals("1M/2M", result.getTextValue());
	assertEquals(Type.os, result.getType());
    }

    @Test
    public void testGetRamError() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns("Some error ocurred", null, "Another error ocurred", null);
	    }
	};

	PropertyValue result = service.getRam();
	assertNull(result);
    }

    @Test
    public void testGetSwapOK() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns(null, "1048576", "2097152", null);
	    }
	};

	PropertyValue result = service.getSwap();
	assertNotNull(result);
	assertEquals(50, result.getPercentage());
	assertTrue(result.getName().contains("Swap"));
	assertEquals("1M/2M", result.getTextValue());
	assertEquals(Type.os, result.getType());
    }

    @Test
    public void testGetSwapError() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns("Some error ocurred", null, "Another error ocurred", null);
	    }
	};

	PropertyValue result = service.getSwap();
	assertNull(result);
    }

    @Test
    public void testGetCpuOK() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns(null, "0.15", null);
	    }
	};

	PropertyValue result = service.getCpu();
	assertNotNull(result);
	assertEquals(15, result.getPercentage());
	assertTrue(result.getName().contains("CPU"));
	assertEquals("15.00%", result.getTextValue());
	assertEquals(Type.os, result.getType());
    }

    @Test
    public void testGetCpuError() throws IOException {
	new NonStrictExpectations() {
	    {
		bufferedReader.readLine();
		returns("Some error ocurred", null, null);
	    }
	};

	PropertyValue result = service.getCpu();
	assertNull(result);
    }
}

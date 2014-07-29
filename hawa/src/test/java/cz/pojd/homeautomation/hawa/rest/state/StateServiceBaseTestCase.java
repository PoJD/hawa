package cz.pojd.homeautomation.hawa.rest.state;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cz.pojd.homeautomation.hawa.rest.state.StateServiceBase;

public class StateServiceBaseTestCase {

    private StateServiceBase service;
    
    @Before
    public void setup() {
	service = new StateServiceBase() {
	};
    }
    
    @Test
    public void testlong2StringMB() {
	String result = service.long2String(1024 * 1024 * 3);
	assertEquals("3M", result);
    }

    @Test
    public void testlong2StringSmallMB() {
	String result = service.long2String(1024);
	assertEquals("0M", result);
    }

    @Test
    public void testlong2StringGB() {
	String result = service.long2String(1024l * 1024l * 1024l * 8);
	assertEquals("8G", result);
    }
}

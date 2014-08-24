package cz.pojd.homeautomation.hawa.refresh;

import static org.junit.Assert.fail;

import java.util.Date;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class RefreshableDAOTestCase {

    private RefreshableDAO dao;

    @Mocked
    private Refresher refresher;

    @Before
    public void setup() {
	dao = new RefreshableDAO() {
	    @Override
	    protected void saveState(Date date) {
	    }

	    @Override
	    protected void detectState() {
	    }
	};
    }

    @Test
    public void testSetRefresher() {
	new NonStrictExpectations() {
	    {
		refresher.register(withEqual(dao));
		times = 1;
	    }
	};
	dao.setRefresher(refresher);
    }

    @Test
    public void testRefreshDetectOKSaveThrowsError() {
	dao = new RefreshableDAO() {
	    @Override
	    protected void saveState(Date date) {
		throw new RuntimeException("Some error in save state");
	    }

	    @Override
	    protected void detectState() {
	    }
	};

	try {
	    dao.refresh(new LocalDateTime());
	} catch (Exception e) {
	    fail("Unexpected exception:" + e.getMessage());
	}
    }

    @Test
    public void testRefreshSaveOKDetectThrowsError() {
	dao = new RefreshableDAO() {
	    @Override
	    protected void saveState(Date date) {
	    }

	    @Override
	    protected void detectState() {
		throw new RuntimeException("Some error in detect state");
	    }
	};

	try {
	    dao.refresh(new LocalDateTime());
	} catch (Exception e) {
	    fail("Unexpected exception:" + e.getMessage());
	}
    }

    @Test
    public void testRefreshDetectOKSaveOK() {
	dao.refresh(new LocalDateTime());
    }
}

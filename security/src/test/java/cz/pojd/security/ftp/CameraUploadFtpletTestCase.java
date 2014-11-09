package cz.pojd.security.ftp;

import java.io.IOException;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Source;

public class CameraUploadFtpletTestCase {

    private static String ROOT_PATH = "/root";
    private static String WATCH_PATH = "/camera/";

    @Mocked
    private Controller controller;
    @Mocked
    private FtpSession session;
    @Mocked
    private FtpRequest request;

    private CameraUploadFtplet cameraUploadFtplet;

    @Before
    public void setup() {
	cameraUploadFtplet = new CameraUploadFtplet(ROOT_PATH + "/", WATCH_PATH);
	cameraUploadFtplet.setSecurityController(controller);
    }

    @Test
    public void testOnUploadEndRequestHasNoArgumentDoesNothing() throws FtpException, IOException {
	new NonStrictExpectations() {
	    {
		request.hasArgument();
		result = false;

		controller.handle(withInstanceOf(SecurityEvent.class));
		maxTimes = 0;
	    }
	};
	cameraUploadFtplet.onUploadEnd(session, request);
    }

    @Test
    public void testOnUploadEndRequestHasNonMatchingArgumentDoesNothing() throws FtpException, IOException {
	new NonStrictExpectations() {
	    {
		request.hasArgument();
		result = true;

		request.getArgument();
		result = "/xxx/";

		controller.handle(withInstanceOf(SecurityEvent.class));
		maxTimes = 0;
	    }
	};
	cameraUploadFtplet.onUploadEnd(session, request);
    }

    @Test
    public void testOnUploadEndRequestHasMatchingArgumentMissingFileExtensionDoesNothing() throws FtpException, IOException {
	new NonStrictExpectations() {
	    {
		request.hasArgument();
		result = true;

		request.getArgument();
		result = WATCH_PATH + Source.CAMERA_MAINDOOR.name().toLowerCase() + "/file.JPG"; // no .part extension here

		controller.handle(withInstanceOf(SecurityEvent.class));
		maxTimes = 0;
	    }
	};
	cameraUploadFtplet.onUploadEnd(session, request);
    }

    @Test
    public void testOnUploadEndRequestHasMatchingArgumentTriggersController() throws FtpException, IOException {
	final String filePath = WATCH_PATH + Source.CAMERA_MAINDOOR.name().toLowerCase() + "/file.JPG";
	new NonStrictExpectations() {
	    {
		request.hasArgument();
		result = true;

		request.getArgument();
		result = filePath + ".part";

		controller.handle(withArgThat(new BaseMatcher<SecurityEvent>() {
		    @Override
		    public boolean matches(Object item) {
			if (!(item instanceof SecurityEvent)) {
			    return false;
			}
			SecurityEvent event = (SecurityEvent) item;
			if (Source.CAMERA_MAINDOOR != event.getSource()) {
			    return false;
			}
			if (!((ROOT_PATH + filePath).equals(event.getFilePath().toString()))) {
			    return false;
			}
			return true;
		    }

		    @Override
		    public void describeTo(Description description) {
			description.appendText(" with argument that is a security event with proper source and filePath");
		    }

		}));
		times = 1;
	    }
	};
	cameraUploadFtplet.onUploadEnd(session, request);
    }
}

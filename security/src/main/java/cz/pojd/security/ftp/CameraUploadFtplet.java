package cz.pojd.security.ftp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;

import cz.pojd.security.controller.Controller;
import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.Source;
import cz.pojd.security.event.Type;

/**
 * Ftplet for camera upload - checks specific path inside ftp root and if that path is updated (e.g. new file uploaded from camera), then this will
 * raise a new security event
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 8:26:42 PM
 */
public class CameraUploadFtplet extends DefaultFtplet {
    private static final Log LOG = LogFactory.getLog(CameraUploadFtplet.class);

    private final String rootPath, watchPath;
    private Controller securityController;

    public Controller getSecurityController() {
	return securityController;
    }

    public void setSecurityController(Controller securityController) {
	this.securityController = securityController;
    }

    @Inject
    public CameraUploadFtplet(String rootPath, String watchPath) {
	this.rootPath = rootPath;
	this.watchPath = watchPath;
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Processing request: " + request);
	}

	if (request.hasArgument() && request.getArgument().startsWith(watchPath)) {
	    Path path = Paths.get(rootPath, request.getArgument()).toAbsolutePath();
	    // top directory is "camera" directory inside is the camera name
	    if (path.getNameCount() > 1) {
		Path cameraDirectory = path.getParent();
		String fileName = path.getFileName().toString();
		if (LOG.isDebugEnabled()) {
		    LOG.debug("Request matches watched path '" + watchPath + "', camera directory: '" + cameraDirectory + "' fileName: '" + fileName);
		}
		if (fileName.endsWith(".part")) { // workaround the fact we have only part file at this very moment
		    Path fullPath = cameraDirectory.resolve(fileName.substring(0, fileName.length() - 5));
		    SecurityEvent event = new SecurityEvent();
		    event.setFilePath(fullPath);
		    event.setType(Type.cameraMotionDetected);
		    event.setSource(Source.parse(cameraDirectory.getFileName().toString()));

		    getSecurityController().handle(event);
		}
	    } else {
		LOG.debug("Request path does not contain subdirectory for camera name, skipping...");
	    }
	}

	return super.onUploadEnd(session, request);
    }
}

package cz.pojd.security.ftp;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.DisposableBean;

/**
 * Manages apache FTP server
 *
 * @author Lubos Housa
 * @since Nov 2, 2014 8:12:00 PM
 */
public class Ftp implements DisposableBean {
    private static final Log LOG = LogFactory.getLog(Ftp.class);

    private FtpServer server;

    public Ftp(int port, UserManager userManager, Ftplet ftplet) {
	LOG.info("Starting up internal FTP server on port :" + port + "...");

	ListenerFactory listenerFactory = new ListenerFactory();
	listenerFactory.setPort(port);
	FtpServerFactory serverFactory = new FtpServerFactory();
	serverFactory.addListener("default", listenerFactory.createListener());
	serverFactory.setFtplets(Collections.singletonMap("security", ftplet));

	serverFactory.setUserManager(userManager);
	server = serverFactory.createServer();
	try {
	    server.start();
	    LOG.info("Internal FTP server started OK...");
	} catch (FtpException e) {
	    LOG.error("FTP server failed to start, snapshots from cameras won't be processed.", e);
	}
    }

    @Override
    public void destroy() throws Exception {
	if (server != null) {
	    LOG.info("Stopping internal FTP server...");
	    server.stop();
	    LOG.info("Internal FTP server stopped.");
	}
    }

}

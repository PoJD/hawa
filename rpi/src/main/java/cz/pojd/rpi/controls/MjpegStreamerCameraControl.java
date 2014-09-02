package cz.pojd.rpi.controls;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Scheduled;

import cz.pojd.rpi.system.RuntimeExecutor;

public class MjpegStreamerCameraControl implements CameraControl, DisposableBean {

    private static final Log LOG = LogFactory.getLog(MjpegStreamerCameraControl.class);

    @Inject
    private RuntimeExecutor runtimeExecutor;

    private final String commandStart, commandEnd;
    private final int cameraAliveTime;

    private LocalDateTime lastTrigger = LocalDateTime.now();
    private boolean running;

    public RuntimeExecutor getRuntimeExecutor() {
	return runtimeExecutor;
    }

    public void setRuntimeExecutor(RuntimeExecutor runtimeExecutor) {
	this.runtimeExecutor = runtimeExecutor;
    }

    public String getCommandStart() {
	return commandStart;
    }

    public String getCommandEnd() {
	return commandEnd;
    }

    public int getCameraAliveTime() {
	return cameraAliveTime;
    }

    public LocalDateTime getLastTrigger() {
	return lastTrigger;
    }

    @Override
    public boolean isRunning() {
	return running;
    }

    @Inject
    public MjpegStreamerCameraControl(String commandStart, String commandEnd, int minutesHeartbeatCamera) {
	this.commandStart = commandStart;
	this.commandEnd = commandEnd;
	this.cameraAliveTime = minutesHeartbeatCamera;
    }

    @Override
    public synchronized boolean startOrPing() {
	if (!isRunning()) {
	    LOG.info("Starting up mjpeg stream to stream camera video using command: " + commandStart);
	    if (!getRuntimeExecutor().executeNoReturn(getCommandStart())) {
		LOG.warn("Unable to start the camera. Assuming is on though...");
	    }
	    running = true;
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Updating last update time in CameraControl - keeping alive...");
	}
	lastTrigger = LocalDateTime.now();
	return true;
    }

    @Override
    public void destroy() throws Exception {
	switchOff();
    }

    private synchronized void switchOff() {
	LOG.info("Ending mjpeg stream for video (if running)...");
	if (isRunning()) {
	    if (!getRuntimeExecutor().executeNoReturn(getCommandEnd())) {
		LOG.warn("Unable to stop the camera...");
	    }
	    running = false;
	}
    }

    @Scheduled(fixedRate = 3 * 60 * 1000)
    private void checkCamera() {
	LOG.info("checkCamera triggered in CameraControl, checking whether the mjpeg streamer is running...");
	if (isRunning()) {
	    if (LOG.isDebugEnabled()) {
		LOG.debug("mjpeg streamer is running, checking whether someone is still watching...");
	    }
	    LocalDateTime now = LocalDateTime.now();
	    if (now.minusMinutes(getCameraAliveTime()).isAfter(getLastTrigger())) {
		LOG.info("More than " + getCameraAliveTime() + " minutes from last hearbeat, switching off the camera...");
		switchOff();
	    }
	}
	LOG.info("checkCamera finished.");
    }
}

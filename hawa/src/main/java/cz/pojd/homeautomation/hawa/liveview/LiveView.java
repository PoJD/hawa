package cz.pojd.homeautomation.hawa.liveview;

public class LiveView {

    private final boolean cameraOK;

    public LiveView(boolean cameraOK) {
	this.cameraOK = cameraOK;
    }

    public boolean getCameraOK() {
	return cameraOK;
    }
}

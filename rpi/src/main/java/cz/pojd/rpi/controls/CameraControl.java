package cz.pojd.rpi.controls;

public interface CameraControl {

    /**
     * Start the camera or ping it to stay alive (sort of a heartbeat)
     * 
     * @return true if OK, false otherwise (e.g. some issue with camera)
     */
    public boolean startOrPing();

    /**
     * Detects whether the camere control is currently running or not
     * 
     * @return true if so, false otherwise
     */
    public boolean isRunning();
}

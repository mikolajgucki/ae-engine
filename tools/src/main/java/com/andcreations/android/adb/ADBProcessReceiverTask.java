package com.andcreations.android.adb;

import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

/**
 * @author Mikolaj Gucki
 */
public class ADBProcessReceiverTask {
    /** */
    private static final String PS_COMMAND = "ps";
    
    /** */
    private static final int DEVICE_POLL_INTERVAL_MSEC = 1000;
    
    /** */
    private ADBDevice device;
    
    /** */
    public ADBProcessReceiverTask(ADBDevice device) {
        this.device = device;
    }
    
    /** */
    public ADBProcess[] listProcesses() throws ADBException {
    // wait for the device
        while (device.getDevice().isOnline() == false) {
            try {
                Thread.sleep(DEVICE_POLL_INTERVAL_MSEC);
            } catch (InterruptedException exception) {
                return null;
            }
        }
        
    // receiver
        PSOutputReceiver receiver = new PSOutputReceiver();
        
    // run command
        try {
            device.getDevice().executeShellCommand(PS_COMMAND,receiver);
        } catch (TimeoutException exception) {
            throw new ADBException(exception);
        } catch (AdbCommandRejectedException ignored) {
            // will not be thrown as long as the shell supports logcat
        } catch (ShellCommandUnresponsiveException ignored) {
            // this will not be thrown since the last argument is 0
        } catch (IOException exception) {
            throw new ADBException(exception);
        }
        
        return receiver.getProcesses();
    }
}
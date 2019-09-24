package com.andcreations.android.adb;

/**
 * @author Mikolaj Gucki
 */
public interface ADBListener {
    /** */
    void adbDeviceConnected(ADBDevice device);
    
    /** */
    void adbDeviceDisconnected(ADBDevice device);
}
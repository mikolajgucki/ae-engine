package com.andcreations.android.adb;

/**
 * @author Mikolaj Gucki
 */
public interface ADBProcessLogCatListener {
    /** */
    void logCatFailed(String processName,Exception exception);
    
    /** */
    void processDied(String processName);
    
    /** */
    void logCatStopped(String processName);
    
}
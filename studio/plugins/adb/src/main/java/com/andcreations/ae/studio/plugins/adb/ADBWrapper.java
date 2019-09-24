package com.andcreations.ae.studio.plugins.adb;

import java.io.File;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.android.AndroidPreferences;
import com.andcreations.ae.studio.plugins.android.AndroidPreferencesListener;
import com.andcreations.android.adb.ADB;
import com.andcreations.android.adb.ADBDevice;
import com.andcreations.android.adb.ADBException;
import com.andcreations.android.adb.ADBListener;
import com.andcreations.android.adb.ADBUtil;

/**
 * @author Mikolaj Gucki
 */
public class ADBWrapper {
    /** */
    private static ADBWrapper instance;
    
    /** */
    private ADB adb;
    
    /** */
    void init() {
        addAndroidPreferencesListener();
        startADB(AndroidPreferences.get().getAndroidSDKDir());
    }
    
    /** */
    private void addAndroidPreferencesListener() {
        AndroidPreferences.get().addAndroidPreferencesListener(
            new AndroidPreferencesListener() {
                /** */
                @Override
                public void androidSDKDirChanged(File oldDir,File newDir) {
                    stopADB();
                    startADB(newDir);
                }
            });        
    }
    
    /** */
    private void startADB(File sdkDir) {
        if (sdkDir == null) {
            Log.info("Skipping ADB start. Android SDK directory not set.");
            return;
        }       
        Log.info(String.format("Starting ADB (%s)",sdkDir.getAbsolutePath()));
        
        if (sdkDir.exists() == false) {
            // TODO Handle.
            return;
        }
        if (sdkDir.isDirectory() == false) {
            // TODO Handle.
            return;
        }
        
    // executable
        File adbExec = ADBUtil.getADBExec(sdkDir);
        if (adbExec == null) {
            Log.info("ADB exec not found (unknown OS)");    
            return;
        }
        Log.info(String.format("ADB exec is %s",adbExec.getAbsolutePath()));
        if (adbExec.exists() == false) {
            Log.info(String.format("ADB exec not found (%s)",
                adbExec.getAbsolutePath()));
            return;
        }
        if (adbExec.isFile() == false) {
            Log.info(String.format("ADB exec is not a file (%s)",
                adbExec.getAbsolutePath()));
            return;
        }
        
        adb = new ADB(adbExec.getAbsolutePath());
        adb.addADBListener(new ADBListener() {
            /** */
            @Override
            public void adbDeviceConnected(ADBDevice device) {
                Log.info(String.format("ADB device connected %s",
                    device.getSerialNumber()));                 
                ADBDevices.get().deviceConnected(device);
                LogCat.get().deviceConnected(device);
            }
            
            /** */
            @Override
            public void adbDeviceDisconnected(ADBDevice device) {
                Log.info(String.format("ADB device disconnected %s",
                    device.getSerialNumber()));
                ADBDevices.get().deviceDisconnected(device);
                LogCat.get().deviceDisconnected(device);
            }            
        });
        
    // start
        try {
            adb.start();
        } catch (ADBException exception) {
            // TODO Handle.
            return;
        }
    }
    
    /** */
    private void stopADB() {
        if (adb == null) {
            return;
        }
        Log.info("Stopping ADB");
        
        adb.terminate();
        adb = null;
    }    
    
    /** */
    void stop() {
        stopADB();
    }
    
    /** */
    public static ADBWrapper get() {
        if (instance == null) {
            instance = new ADBWrapper();
        }
        
        return instance;
    }
}
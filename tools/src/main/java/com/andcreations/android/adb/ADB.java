package com.andcreations.android.adb;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.resources.BundleResources;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

/**
 * The wrapper for the ddmlib.
 *
 * @author Mikolaj Gucki
 */
public class ADB {
    /** */
    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    
    /** */
    private BundleResources res = new BundleResources(ADB.class);    
    
    /** */
    private String adbExecPath;
    
    /** */
    private int connectionTimeout = CONNECTION_TIMEOUT;
    
    /** */
    private AndroidDebugBridge adb;
    
    /** */
    private List<ADBListener> listeners = new ArrayList<>();
    
    /** */
    private List<ADBDevice> devices = new ArrayList<>();
    
    /** */
    public ADB(String adbExecPath) {
        this.adbExecPath = adbExecPath;
    }
    
    /** */
    public void addADBListener(ADBListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void removeADBListener(ADBListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    private ADBDevice findADBDevice(IDevice device) {
        for (ADBDevice adbDevice:devices) {
            if (adbDevice.getDevice() == device) {
                return adbDevice;
            }
        }
        
        return null;
    }
    
    /** */
    private void deviceConnected(IDevice device) {
        ADBDevice adbDevice = new ADBDevice(device);
        devices.add(adbDevice);
        
        synchronized (listeners) {
            for (ADBListener listener:listeners) {
                listener.adbDeviceConnected(adbDevice);
            }
        }
    }
    
    /** */
    private void deviceDisconnected(IDevice device) {
        ADBDevice adbDevice = findADBDevice(device);
        devices.remove(adbDevice);
        
        synchronized (listeners) {
            for (ADBListener listener:listeners) {
                listener.adbDeviceDisconnected(adbDevice);
            }
        }
    }
    
    /** */
    private boolean waitForConnection() {
        long then = System.currentTimeMillis();
        while (true) {
            if (adb.isConnected()) {
                return true;
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
            }
            
            long now = System.currentTimeMillis();
            if (now - then > connectionTimeout) {
                break;
            }
        }
        return false;
    }    
    
    /** */
    public void start() throws ADBException {
        AndroidDebugBridge.init(false);
        
    // create and connect
        adb = AndroidDebugBridge.createBridge(adbExecPath,false);
        if (waitForConnection() == false) {
            throw new ADBException(res.getStr("connection.timeout"));
        }
        
        AndroidDebugBridge.addDeviceChangeListener(new IDeviceChangeListener() {
            /** */
            @Override
            public void deviceConnected(IDevice device) {
                ADB.this.deviceConnected(device);
            }
            
            /** */
            @Override
            public void deviceDisconnected(IDevice device) {
                ADB.this.deviceDisconnected(device);
            }
            
            /** */
            @Override
            public void deviceChanged(IDevice device,int changeMask) {
            }
        });
        
    // list devices
        for (IDevice device:adb.getDevices()) {
            deviceConnected(device);
        }
    }
    
    /** */
    public void terminate() {
        AndroidDebugBridge.terminate();
    }
}
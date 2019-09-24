package com.andcreations.ae.ddm;

import java.util.List;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.logcat.LogCatReceiverTask;

/**
 * @author Mikolaj Gucki
 */
public class DDM {
    /** */
    private static void p(String msg) {
        System.out.println(msg);
    }
    
    /** */
    private static boolean waitForConnection(AndroidDebugBridge adb,int timeout) {
        long startTime = System.currentTimeMillis();
        while (true) {
            if (adb.isConnected()) {
                return true;
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
            }
            
            long now = System.currentTimeMillis();
            if (now - startTime > timeout) {
                break;
            }
        }
        return false;
    }
    
    /** */
    private static String getName(IDevice device) {
        String model = device.getProperty(IDevice.PROP_DEVICE_MODEL);
        return String.format("%s",model);
    }
    
    /** */
    private static void logcat(IDevice device) {
        LogCatReceiverTask task = new LogCatReceiverTask(device);
        task.addLogCatListener(new LogCatListener() {
            @Override   
            public void log(List<LogCatMessage> msgList) { 
                for (LogCatMessage logCatMsg:msgList) {
                    p(logCatMsg.toString());
                }
            }
        });
        task.run();
    }
    
    /** */
    public static void main(String[] args) throws Exception {
        AndroidDebugBridge.init(false);
        AndroidDebugBridge adb = AndroidDebugBridge.createBridge(
            "c:\\users\\mikolaj\\.jedit\\android-sdk-windows\\platform-tools\\adb.exe",false);
        p("Bridge created");
        if (waitForConnection(adb,10 * 1000) == false) {
            p("Failed to connect");
            return;
        }
        
        p("Devices:");
        IDevice[] devices = adb.getDevices();
        for (IDevice device:devices) {
            p(String.format("  %s",getName(device)));
        }
        if (devices.length > 0) {
            logcat(devices[0]);
        }
        
        AndroidDebugBridge.terminate();
        p("Terminated");
    }
}
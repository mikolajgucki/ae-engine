package com.andcreations.android.adb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** */
public class LogCatTest {
    /** */
    private static Map<String,ADBProcessLogCat> logCatMap =
        new HashMap<String,ADBProcessLogCat>();
    
    /** */
    private static void logCat(ADBDevice device,String processName) {
        ADBProcessLogCat logCat = new ADBProcessLogCat(device,
            processName,new ADBLogCatListener() {
                /** */
                @Override
                public void log(List<ADBLogCatMessage> messages) {
                    for (ADBLogCatMessage msg:messages) {
                        System.out.println(msg.getMessage());
                    }
                }
        });
        logCatMap.put(device.getSerialNumber(),logCat);
        logCat.addADBProcessLogCatListener(new ADBProcessLogCatListener() {
            /** */
            public void logCatFailed(String processName,Exception exception) {
            }
            
            /** */
            public void processDied(String processName) {
                System.err.println("Died!");
            }
            
            /** */
            public void logCatStopped(String processName) {
                System.err.println("Stopped!");
            }
        });
        logCat.startLogCat();
        
        /*
        try {           
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
        }
        System.out.println("stopping logcat");
        logCat.stopLogCat();
        */
    }
    
    /** */
    public static void main(String[] args) throws Exception {
        ADB adb = new ADB("/Users/ac/programs/android-sdk-macosx/platform-tools/adb");
        adb.addADBListener(new ADBListener() {
            /** */
            public void adbDeviceConnected(ADBDevice device) {
                System.out.println("connected " + device);
                synchronized (logCatMap) {
                    logCat(device,"com.andcreations.bubble");
                }
            }
            
            /** */
            public void adbDeviceDisconnected(ADBDevice device) {
                System.out.println("disconnected " + device);
                synchronized (logCatMap) {
                    ADBProcessLogCat logCat = logCatMap.get(device.getSerialNumber());
                    if (logCat != null) {
                        logCat.stopLogCat();
                    }
                }
            }            
        });
        adb.start();
    }
}
package com.andcreations.android.adb;

import java.io.File;

import com.andcreations.system.OS;

/**
 * @author Mikolaj Gucki
 */
public class ADBUtil {
    /** */
    public static File getADBExec(File androidSDKDir) {
        if (OS.getOS() == OS.WINDOWS) {
            return new File(androidSDKDir,"platform-tools/adb.exe");
        }
        if (OS.getOS() == OS.LINUX || OS.getOS() == OS.OSX) {
            return new File(androidSDKDir,"platform-tools/adb");
        }
        
        return null;
    }    
}
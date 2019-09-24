package com.andcreations.ae;

import android.os.Build.VERSION;
import android.app.Activity;
import android.content.pm.ApplicationInfo;

import com.andcreations.ae.Log;

/**
 * @author Mikolaj Gucki
 */
public class AE {
    /** */
    private static final String TAG = "AE";    
    
    /** The debug flag. */
    private static boolean debugFlag;
    
    /** Indicates if to force debug event if running a release version. */
    private static boolean forceDebug;
    
    /** */
    public static native void initDebug();
    
    /** */
    private static void getDebugFlag(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        debugFlag = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        if (forceDebug == true) {
            debugFlag = true;
        }
    }
    
    /**
     * Indicates if the engine is running in the debug mode.
     * @return <code>true</code> if running in debug mode,
     *   <code>false</code> otherwise.
     */
    public static boolean isDebug() {
        return debugFlag;
    }
    
    /**
     * Forces to run the engine in debug mode even if running a release version.
     * It should be used to debug a release. Call this method from onCreate()
     * before super.onCreate().
     */
    public static void forceDebug() {
        forceDebug = true;
    }
    
    /** */
    public static void onCreate(Activity activity) {
        Log.d(TAG,"onCreate()");
        
        getDebugFlag(activity); 
        if (isDebug()) {
            initDebug();
        }
        
    // audio
        AEAudio.onCreate(activity);
    }
    
    /** */
    public static void onPause(Activity activity) {
        AEAudio.onPause();
    }
    
    /** */
    public static void onResume(Activity activity) {
        AEAudio.onResume();
    }
    
    /** */
    public static int getSDK() {
        return VERSION.SDK_INT;
    }
}
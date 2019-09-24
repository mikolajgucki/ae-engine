package com.andcreations.vungle;

import android.app.Activity;
import com.vungle.publisher.VunglePub;

import com.andcreations.ae.Log;

/**
 * Vungle integration.
 *
 * @author Mikolaj Gucki
 */
public class AEVungle {
    /** The logcat tag. */
    private static final String TAG = "AE/Vungle";
    
    /** The activity. */
    private static Activity activity;    
    
    /** Vungle. */
    private static VunglePub vunglePub;
    
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();    
    
    /** */
    public static native void onAdPlayableChanged(boolean isAdPlayable); 
    
    /** */
    public static native void willShowAd(); 
    
    /** */
    public static native void willCloseAd(); 
    
    /** */
    public static native void adClicked(); 
    
    /** */
    public static native void viewCompleted(); 
    
    /**
     * Initializes Vungle. Must be called from onCreate().
     *
     * @param activity The activity.
     * @param appId The application identifier.     
     */
    public static void init(Activity activity,String appId) {
        Log.d(TAG,"AEVungle.init()");
        AEVungle.activity = activity;
        
    // initialize
        vunglePub = VunglePub.getInstance();
        if (vunglePub.init(activity,appId) == false) {
            Log.e(TAG,"Failed to initialize Vungle");
            return;
        }
        
    // listener
        vunglePub.addEventListeners(new AEVungleListener());
    }
    
    /** */
    public static void onPause() {
        vunglePub.onPause();
    }
    
    /** */
    public static void onResume() {
        vunglePub.onResume();
    }
    
    /**
     * Plays an ad.
     */
    public static void playAd() {
        activity.runOnUiThread(new Runnable() {
            /** */
            @Override
            public void run() {
                Log.d(TAG,"AEVungle.playAd()");
                vunglePub.playAd();
            }
        });
    }
}
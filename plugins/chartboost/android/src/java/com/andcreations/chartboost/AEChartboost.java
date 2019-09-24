package com.andcreations.chartboost;

import android.app.Activity;
import com.chartboost.sdk.Chartboost;

import com.andcreations.ae.Log;

/**
 * Chartboost integration.
 *
 * @author Mikolaj Gucki
 */
public class AEChartboost {
    /** The logcat tag.*/
    private static final String TAG = "AE/Chartboost";    
    
    /** The activity. */
    private static Activity activity;
    
    /** The Chartboost delegate. */
    private static AEChartboostDelegate delegate;
            
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();
    
    /** */
    public static native void didCacheInterstitial(String location);    
    
    /** */
    public static native void didFailToLoadInterstitial(String location);    
    
    /** */
    public static native void didClickInterstitial(String location);    
    
    /** */
    public static native void didCloseInterstitial(String location);    
    
    /** */
    public static native void didDismissInterstitial(String location);    
    
    /** */
    public static native void didDisplayInterstitial(String location);    
    
    /**
     * Initializes Chartboost.
     *
     * @param activity The activity.
     * @param appId The application identifier.
     * @param appSignature The application signature.
     */
    public static void init(Activity activity,String appId,
        String appSignature) {
    //
        AEChartboost.activity = activity;
    
    // start
       Chartboost.startWithAppId(activity,appId,appSignature);
       Chartboost.onCreate(activity);
       
   // delegate
       delegate = new AEChartboostDelegate();
       Chartboost.setDelegate(delegate);
    }
    
    /**
     * Caches an interstitial ad.
     *
     * @param location The location.
     */
    public static void cacheInterstitial(final String location) {
        activity.runOnUiThread(new Runnable() {
            /** */
            @Override
            public void run() {
                Log.d(TAG,"AEChartboost.cacheInterstitial(" + location + ")");
                Chartboost.cacheInterstitial(location);
            }
        });
    }
    
    /**
     * Shows an interstitial ad.
     *
     * @param location The location.
     */
    public static void showInterstitial(String location) {
        Log.d(TAG,"AEChartboost.showInterstitial(" + location + ")");
        Chartboost.showInterstitial(location);
    }
}
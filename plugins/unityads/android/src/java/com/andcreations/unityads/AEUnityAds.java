package com.andcreations.unityads;

import android.app.Activity;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAds.FinishState;
import com.unity3d.ads.UnityAds.UnityAdsError;

import com.andcreations.ae.Log;

/**
 * Unity Ads integration.
 *
 * @author Mikolaj Gucki
 */
public class AEUnityAds {
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib(); 
    
    /** */
    public static native void setReady(String placementId);
    
    /** */
    public static native void started(String placementId);
    
    /** */
    public static native void finished(String placementId,String state);
    
    /** */
    public static native void failed(String error,String msg);

    /** */
    private static final String TAG = "AE/UnityAds";
    
    /** */
    private static boolean supported;

    /** */
    private static Activity activity;
    
    /**
     * Initializes the Unity Ads.
     *
     * @param activity The activity.
     * @param gameId The game identifier.
     * @param debugMode The debug mode flag.
     */
    public static void init(Activity activity,String gameId,boolean debugMode) {
        supported = UnityAds.isSupported();
        if (supported == false) {
            Log.d(TAG,"Unity Ads not supported");
            return;
        }
        AEUnityAds.activity = activity;
        
    // listener
        IUnityAdsListener listener = new IUnityAdsListener() {
            /** */
            @Override
            public void onUnityAdsReady(String placementId) {
                setReady(placementId);
            }
            
            /** */
            @Override
            public void onUnityAdsStart(String placementId) {
                started(placementId);
            }
            
            /** */
            @Override
            public void onUnityAdsFinish(String placementId,FinishState state) {
                String stateStr = null;
                switch (state) {
                    case ERROR:
                        stateStr = "error";
                        break;
                        
                    case SKIPPED:
                        stateStr = "skipped";
                        break;
                        
                    case COMPLETED:
                        stateStr = "completed";
                        break;
                }
                
                finished(placementId,stateStr);
            }
            
            /** */
            @Override
            public void onUnityAdsError(UnityAdsError error,String msg) {
                failed(error.toString(),msg);
            }
        };
    
    // initialize
        UnityAds.initialize(activity,gameId,listener);
        UnityAds.setDebugMode(debugMode);
    }
    
        /**
     * Initializes the Unity Ads.
     *
     * @param activity The activity.
     * @param gameId The game identifier.
     */
    public static void init(Activity activity,String gameId) {
        init(activity,gameId,false);
    }
    
    /** */
    public static boolean isReady() {
        if (supported == false) {
            return false;
        }
        return UnityAds.isReady();
    }
    
    /** */
    public static boolean isReady(String placementId) {
        return UnityAds.isReady(placementId);
    }
    
    /** */
    public static void show() {
        if (supported == false) {
            return;
        }
        UnityAds.show(activity);
    }
    
    /** */
    public static void show(String placementId) {
        if (supported == false) {
            return;
        }
        UnityAds.show(activity,placementId);
    }
}
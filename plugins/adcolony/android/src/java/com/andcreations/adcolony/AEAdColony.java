package com.andcreations.adcolony;

import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyZone;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;

import com.andcreations.ae.Log;

/**
 * AdColony integration.
 *
 * @author Mikolaj Gucki
 */
public class AEAdColony {
    /** The log tag. */
    private static final String TAG = "AE/AdColony";
    
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();
    
    /**
     * Called when an interstitial is available.
     *
     * @param zoneId The zone identifier.
     */
    public static native void interstitialAvailable(String zoneId);
    
    /**
     * Called when no interstitial is available.
     *
     * @param zoneId The zone identifier.
     */
    public static native void interstitialUnavailable(String zoneId);
    
    /**
     * Called when an interstitial has been opened.
     *
     * @param zoneId The zone identifier.
     */
    public static native void interstitialOpened(String zoneId);
    
    /**
     * Called when an interstitial has been closed.
     *
     * @param zoneId The zone identifier.
     */
    public static native void interstitialClosed(String zoneId);
    
    /** */
    private static Map<String,AdColonyInterstitial> interstitials =
        new HashMap<String,AdColonyInterstitial>();
    
    /**
     * Initializes the AdColony.
     *
     * @param activity The activity.
     * @param appId The application identifier.
     * @param zoneIds The zone identifiers.
     */
    public static void init(Activity activity,String appId,String... zoneIds) {
        AdColony.configure(activity,appId,zoneIds);
    }

    /**
     * Requests an interstitial.
     *
     * @param zoneId The zone identifier.
     */
    public static void requestInterstitial(final String zoneId) {
        Log.d(TAG,"AEAdColony.requestInterstitial(" + zoneId + ")");
        
    // listener
        AdColonyInterstitialListener listener =
            new AdColonyInterstitialListener() {
                /** */
                @Override
                public void onRequestFilled(AdColonyInterstitial ad) {
                    Log.d(TAG,"onRequestFilled(" + zoneId + ")");
                    interstitials.put(zoneId,ad);
                    interstitialAvailable(zoneId);
                }
                
                /** */
                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    Log.d(TAG,"onRequestNotFilled(" + zoneId + ")");
                    interstitials.remove(zoneId);
                    interstitialUnavailable(zoneId);
                }
                
                /** */
                @Override
                public void onOpened(AdColonyInterstitial ad) {
                    Log.d(TAG,"onOpened(" + zoneId + ")");
                    interstitialOpened(zoneId);
                }
                
                /** */
                @Override
                public void onClosed(AdColonyInterstitial ad) {
                    Log.d(TAG,"onClosed(" + zoneId + ")");
                    interstitialClosed(zoneId);
                }
            };
            
    // request
        AdColony.requestInterstitial(zoneId,listener);
    }
    
    /** */
    public static void showInterstitial(String zoneId) {
        AdColonyInterstitial ad = interstitials.get(zoneId);
        if (ad == null) {
            return;
        }
        if (ad.isExpired() == true) {
            Log.w(TAG,"Attempt to show an expired interstitial");
            return;
        }
        
        ad.show();
    }
    
    /** */
    public static boolean isInterstitialExpired(String zoneId) {
        AdColonyInterstitial ad = interstitials.get(zoneId);
        if (ad == null) {
            return true;
        }
        
        return ad.isExpired();
    }
}
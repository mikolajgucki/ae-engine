package com.andcreations.tapjoy;

import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import android.app.Activity;

import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJError;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJPlacementListener;

import com.andcreations.ae.Log;

/**
 * @author Mikolaj Gucki
 */
public class AETapjoy {
    /** The log tag. */
    private static final String TAG = "AE/Tapjoy";    

    /** */
    private static TJPlacementListener listener = new TJPlacementListener() {
        /** */
        public void	onRequestSuccess(TJPlacement placement) {
            Log.d(TAG,"onRequestSuccess(" + placement.getName() + ")");
            requestSucceeded(placement.getName());
        }
        
        /** */
        public void	onRequestFailure(TJPlacement placement,TJError error) {
            Log.d(TAG,"onRequestFailure(" + placement.getName() + "," +
                error.message + ")");
            requestFailed(placement.getName(),error.message);
        }
        
        /** */
        public void	onContentReady(TJPlacement placement) {
            Log.d(TAG,"onContentReady(" + placement.getName() + ")");
            contentIsReady(placement.getName());
        }
        
        /** */
        public void	onContentShow(TJPlacement placement) {
            Log.d(TAG,"onContentShow(" + placement.getName() + ")");
            contentShown(placement.getName());
        }
        
        /** */
        public void	onContentDismiss(TJPlacement placement) {
            Log.d(TAG,"onContentDismiss(" + placement.getName() + ")");
            contentDismissed(placement.getName());
        }
        
        /** */
        public void	onPurchaseRequest(TJPlacement placement,
            TJActionRequest request,String productId) {
        }
        
        /** */
        public void	onRewardRequest(TJPlacement placement,
            TJActionRequest request,String itemId,int quantity) {
        }            
    };    
    
    /** */
    private static Activity activity;
        
    /**
     * Loads the Lua library.
     */    
    public static native void loadLuaLib();
        
    /** */
    private static native void connectionSucceeded();
    
    /** */
    private static native void connectionFailed();    
    
    /** */
    private static native void requestSucceeded(String placement);
    
    /** */
    private static native void requestFailed(String placement,String error);
    
    /** */
    private static native void contentIsReady(String placement);
    
    /** */
    private static native void contentShown(String placement);
    
    /** */
    private static native void contentDismissed(String placement);
    
    /**
     * Makes the connect call.
     *
     * @param activity The activity.
     * @param sdkKey The SDK key.
     * @param debugEnabled The debug flag.
     */
    public static void connect(Activity activity,String sdkKey,
        boolean debugEnabled) {
    //
        Log.d(TAG,"connect()");
        AETapjoy.activity = activity;
        
    // listener
        TJConnectListener listener = new TJConnectListener() {
            /** */
            @Override
            public void onConnectSuccess() {
                Log.d(TAG,"onConnectSuccess()");
                connectionSucceeded();
            }
            
            /** */
            @Override
            public void onConnectFailure() {
                Log.d(TAG,"onConnectFailure()");
                connectionFailed();
            }
        };
    
    // connect
        Hashtable<String,Object> flags = new Hashtable<String,Object>();
        flags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true");
        Tapjoy.connect(activity,sdkKey,flags,listener);
        
    // debug
        Tapjoy.setDebugEnabled(debugEnabled);
    }
    
    /**
     * Initializes the push notifications.
     *
     * @param gcmSender The GCM sender.
     */
    public static void initPush(String gcmSender) {
        Log.d(TAG,"AETapjoy.initPush(" + gcmSender + ")");
        Tapjoy.setGcmSender(gcmSender);
    }
    
    /**
     * Makes the connect call.
     *
     * @param activity The activity.
     * @param sdkKey The SDK key.
     */
    public static void connect(Activity activity,String sdkKey) {
        connect(activity,sdkKey,false);
    }
    
    /** */
    public static void onStart() {
        Tapjoy.onActivityStart(activity);
    }
    
    /** */
    public static void onStop() {
        Tapjoy.onActivityStop(activity);
    }
    
    /** */    
    public static boolean isConnected() {
        return Tapjoy.isConnected();
    }
    
    /** */
    public static void requestContent(String placementName) {
        Log.d(TAG,"requestContent(" + placementName + ")");    
        
    // check connected
        if (isConnected() == false) {
            Log.w(TAG,"Tapjoy disconnected. Cannot request content.");
            return;
        }
        
    // get placement
        TJPlacement placement = Tapjoy.getPlacement(placementName,listener);
        
    // request content
        placement.requestContent();
    }
    /** */
    public static boolean isContentReady(String placementName) {
        Log.d(TAG,"isContentReady(" + placementName + ")");
        return false;
    }
    
    /** */
    public static void showContent(String placementName) {
        Log.d(TAG,"showContent(" + placementName + ")");
        
    // get placement
        TJPlacement placement = Tapjoy.getPlacement(placementName,listener);
        
    // show content
        placement.showContent();
    }
}
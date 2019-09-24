package com.andcreations.gameanalytics;

import android.app.Activity;
import android.os.Build;
import com.gameanalytics.sdk.GAProgressionStatus;
import com.gameanalytics.sdk.GAErrorSeverity;
import com.gameanalytics.sdk.GAResourceFlowType;
import com.gameanalytics.sdk.GAPlatform;
import com.gameanalytics.sdk.GameAnalytics;

import com.andcreations.ae.Log;

/**
 * Game Analytics integration.
 *
 * @author Mikolaj Gucki
 */
public class AEGameAnalytics {
    /** The logcat tag. */
    private static final String TAG = "AE/GameAnalytics"; 

    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();
    
    /** The activity. */
    private static Activity activity;
    
    /**
     * Enables logging. Must be called before {@link #init()}.
     */
    public static void enableLogging() {
        GameAnalytics.setEnabledInfoLog(true);
        GameAnalytics.setEnabledVerboseLog(true);        
    }
    
    /**
     * Configures the available resource currencies. Must be called before
     * {@link #init()}.
     *
     * @param currencies The currencies.
     */
    public static void configureAvailableResourceCurrencies(
        String... currencies) {
    //
        GameAnalytics.configureAvailableResourceCurrencies(currencies);
    }
    
    /**
     * Configures the available resource item types. Must be called before
     * {@link #init()}.
     *
     * @param itemTypes The item types.
     */
    public static void configureAvailableResourceItemTypes(
        String... itemTypes) {
    //
        GameAnalytics.configureAvailableResourceItemTypes(itemTypes);
    }
    
    /**
     * Initializes Game Analytics. Must be called from onCreate().
     *
     * @param activity The acvitity.
     * @param gameKey The game key.
     * @param secretKey The secret key.
     */
    public static void init(Activity activity,String gameKey,String secretKey) {
        AEGameAnalytics.activity = activity;
        GameAnalytics.configureBuild("android 1.0.0");
        GameAnalytics.initializeWithGameKey(activity,gameKey,secretKey);       
    }
    
    /** */
    public static void onResume() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            GAPlatform.onActivityResumed(activity);
        }
    }
    
    /** */
    public static void onPause() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            GAPlatform.onActivityPaused(activity);
        }
    }
    
    /** */
    public static void onStop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            GAPlatform.onActivityStopped(activity);
        }
    }    
    
    /** */
    private static String getGAString(String str) {
        return str != null ? str : "";
    }
    
    /**
     * Adds a progression event. Called from Lua.
     *
     * @param status The progression status.
     * @param progression01 The progression.
     */
    public static void addProgressionEvent(String status,
        String progression01) {
    //
        addProgressionEvent(status,progression01,null,null);
    }    
    
    /**
     * Adds a progression event. Called from Lua.
     *
     * @param status The progression status.
     * @param progression01 The progression.
     * @param progression02 The progression.
     */
    public static void addProgressionEvent(String status,
        String progression01,String progression02) {
    //
        addProgressionEvent(status,progression01,progression02,null);
    }    
    
    /**
     * Adds a progression event. Called from Lua.
     *
     * @param status The progression status.
     * @param progression01 The progression.
     * @param progression02 The progression.
     * @param progression03 The progression.
     */
    public static void addProgressionEvent(String status,
        String progression01,String progression02,String progression03) {
    //
        Log.d(TAG,String.format(
            "AEGameAnalytics.addProgressionEvent(%s,%s,%s,%s)",
            status,progression01,progression02,progression03));
            
    // status
        GAProgressionStatus gaStatus;
        if ("start".equals(status) == true) {
            gaStatus = GAProgressionStatus.Start;
        }
        else if ("complete".equals(status) == true) {
            gaStatus = GAProgressionStatus.Complete;
        }
        else if ("fail".equals(status) == true) {
            gaStatus = GAProgressionStatus.Fail;
        } else {
            Log.e(TAG,"Unknown progression status " + status);
            return;
        }
        
    // add event
        GameAnalytics.addProgressionEventWithProgressionStatus(gaStatus,
            getGAString(progression01),getGAString(progression02),
            getGAString(progression03));
    }    
    
    /**
     * Adds a design event. Called from Lua.
     *
     * @param eventId The event identifier.
     */
    public static void addDesignEvent(String eventId) {
        Log.d(TAG,String.format(
            "AEGameAnalytics.addDesignEvent(%s)",eventId));
        GameAnalytics.addDesignEventWithEventId(eventId);
    }
    
    /**
     * Adds a design event. Called from Lua.
     *
     * @param eventId The event identifier.
     * @param value The event value.
     */
    public static void addDesignEvent(String eventId,double value) {
        Log.d(TAG,String.format(
            "AEGameAnalytics.addDesignEvent(%s,%f)",eventId,value));
        GameAnalytics.addDesignEventWithEventId(eventId,(float)value);
    }
    
    /**
     * Adds an error event. Called from Lua.
     *
     * @param severity The severity.
     * @param msg The message.
     */
    public static void addErrorEvent(String severity,String msg) {
        Log.d(TAG,String.format("AEGameAnalytics.addErrorEvent(%s,%s)",
            severity,msg));
        
    // severity
        GAErrorSeverity gaSeverity = null;
        if ("debug".equals(severity) == true) {
            gaSeverity = GAErrorSeverity.Debug;
        } else if ("info".equals(severity) == true) {
            gaSeverity = GAErrorSeverity.Info;
        } else if ("warning".equals(severity) == true) {
            gaSeverity = GAErrorSeverity.Warning;
        } else if ("error".equals(severity) == true) {
            gaSeverity = GAErrorSeverity.Error;
        } else if ("critical".equals(severity) == true) {
            gaSeverity = GAErrorSeverity.Critical;
        } else {
            Log.e(TAG,"Unknown error severity " + severity);
            return;
        }
        
    // add event
        GameAnalytics.addErrorEventWithSeverity(gaSeverity,msg);
    }
    
    /**
     * Adds a business event.
     *
     * @param currency The currency in ISO 4217 format.
     * @param amount The amount in cents.
     * @param itemType The item type.
     * @param itemId The item identifier.
     * @param cartType The cart type.
     * @param receipt The transaction receipt.
     * @param store The app store (must be google_play).
     * @param signature The transaction receipt signature.
     */
    public static void addBusinessEvent(String currency,int amount,
        String itemType,String itemId,String cartType,String receipt,
        String store,String signature) {
    //
        Log.d(TAG,String.format("AEGameAnalytics.addBusinessEvent(%s,%d,%s" +
            ",%s,%s,%d,%d,%s)",currency,amount,itemType,itemId,cartType,
            receipt,store,signature));
        GameAnalytics.addBusinessEventWithCurrency(currency,amount,itemType,
            itemId,cartType,getGAString(receipt),getGAString(store),
            getGAString(signature));
    }
    
    /**
     * Adds a resource event.
     *
     * @param flowType The flow type.
     * @param currency The currency.
     * @param amount The amount.
     * @param itemType The item type.
     * @param itemId The item identifier.
     */
    public static void addResourceEvent(String flowType,String currency,
        float amount,String itemType,String itemId) {
    //
        Log.d(TAG,String.format("AEGameAnalytics.addResourceEvent(%s,%s,%f," +
            "%s,%s)",flowType,currency,amount,itemType,itemId));
        
    // flow type
        GAResourceFlowType gaFlowType = null;
        if ("sink".equals(flowType) == true) {
            gaFlowType = GAResourceFlowType.Sink;
        } else if ("source".equals(flowType) == true) {
            gaFlowType = GAResourceFlowType.Source;
        } else {
            Log.e(TAG,"Unknown flow type " + flowType);
            return;            
        }
        
    // add event
        GameAnalytics.addResourceEventWithFlowType(
            gaFlowType,currency,amount,itemType,itemId);
    }
}
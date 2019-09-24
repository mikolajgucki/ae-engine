package com.andcreations.flurry;

import java.util.Map;
import android.app.Activity;
import com.flurry.android.FlurryAgent;

import com.andcreations.ae.Log;

/**
 * Flurry integration.
 *
 * @author Mikolaj Gucki
 */
public class AEFlurry {
    /** The logcat tag. */
    private static final String TAG = "AE/Flurry";    
    
    /** The activity. */
    private static Activity activity;
    
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();     
    
    /**
     * Initializes Flurry. Must be called from onCreate().
     *
     * @param activity The acvitity.
     * @param apiKey The API key.
     */
    public static void init(Activity activity,String apiKey) {
        AEFlurry.activity = activity;
        FlurryAgent.init(activity,apiKey);
    }
    
    /** */
    public static void onStart() {
        FlurryAgent.onStartSession(activity);
    }
    
    /** */
    public static void onStop() {
        FlurryAgent.onEndSession(activity);
    }
    
    /**
     * Logs an event. Called from Lua.
     *
     * @param eventId The event identifier.
     * @param timed Indicates if the event is timed.
     */
    public static void logEvent(String eventId,boolean timed) {
        Log.d(TAG,"AEFlurry.logEvent(" + eventId + "," + timed + ")");
        FlurryAgent.logEvent(eventId,timed);
    }
    
    
    /**
     * Logs an event. Called from Lua.
     *
     * @param eventId The event identifier.
     * @param parameters The event parameters.
     * @param timed Indicates if the event is timed.
     */
    public static void logEvent(String eventId,Map<String,String> parameters,
        boolean timed) {
    //
        Log.d(TAG,"AEFlurry.logEvent(" + eventId + "," + timed +
            ") with parameters");
        FlurryAgent.logEvent(eventId,parameters,timed);
    }    
    
    /**
     * Ends a timed event.
     *
     * @param eventId The event identifier.
     */
    public static void endTimedEvent(String eventId) {
        Log.d(TAG,"AEFlurry.endTimedEvent(" + eventId + ")");
        FlurryAgent.endTimedEvent(eventId);
    }
    
    /**
     * Ends a timed event.
     *
     * @param eventId The event identifier.
     * @param parameters The event parameters.
     */
    public static void endTimedEvent(String eventId,
        Map<String,String> parameters) {
    //
        Log.d(TAG,"AEFlurry.endTimedEvent(" + eventId + ") with parametrs");
        FlurryAgent.endTimedEvent(eventId,parameters);
    }
}
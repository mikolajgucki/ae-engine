package com.andcreations.http;

import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import com.andcreations.ae.Log;

/**
 * HTTP intergration.
 * 
 * @author Mikolaj Gucki
 */
public class AEHTTP {
    /** The logcat tag. */
    private static final String TAG = "AE/HTTP";
    
    /** The connection timeout in milliseconds. */
    static final int CONNECT_TIMEOUT = 10 * 1000;
    
    /** The read timeout in milliseconds. */
    static final int READ_TIMEOUT = 10 * 1000;

    /** */
    private static Activity activity;
    
    /**
     * Loads the Lua library.
     */    
    public static native void loadLuaLib();
    
    /**
     * Called when a response has been received. 
     */
    public static native void receivedResponse(String id,int code,String body);
    
    /**
     * Called when it failed to receive a response.
     */
    public static native void failedToReceiveResponse(
        String id,String errorMsg);
    
    /**
     * Initializes the HTTP library.
     *
     * @param activity The activity.
     */
    public static void init(Activity activity) {
        AEHTTP.activity = activity;
    }
    
    /** */
    public static void sendGETRequest(String id,String url) {
        Log.d(TAG,String.format("AEHTTP.sendGETRequest(%s,%s)",id,url));
        
        HTTPGETRequest request = new HTTPGETRequest(id,url);
        Thread thread = new Thread(request,"AE/HTTP");
        thread.start();
    }
    
    /** */
    public static void openURL(String url) {
        Log.d(TAG,String.format("AEHTTP.openURL(%s)",url));
        
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        activity.startActivity(intent);        
    }
}
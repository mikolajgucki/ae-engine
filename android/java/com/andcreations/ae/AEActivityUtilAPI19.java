package com.andcreations.ae;

import android.view.View;

/**
 * @author Mikolaj Gucki
 */
public class AEActivityUtilAPI19 {
    /** The log tag. */
    private static final String TAG = AEActivityUtilAPI19.class.getName();
    
    /** */
    static int getFullScreenFlags() {
        Log.d(TAG,"getFullScreenFlags()");
        return View.SYSTEM_UI_FLAG_IMMERSIVE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }
}
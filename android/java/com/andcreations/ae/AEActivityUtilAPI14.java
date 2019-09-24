package com.andcreations.ae;

import android.view.View;

/**
 * @author Mikolaj Gucki
 */
public class AEActivityUtilAPI14 {
    /** The log tag. */
    private static final String TAG = AEActivityUtilAPI14.class.getName();
    
    /** */
    static int getFullScreenFlags() {
        Log.d(TAG,"getFullScreenFlags()");
        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
}
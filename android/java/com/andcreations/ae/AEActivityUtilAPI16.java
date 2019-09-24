package com.andcreations.ae;

import android.view.View;

/**
 * @author Mikolaj Gucki
 */
public class AEActivityUtilAPI16 {
    /** The log tag. */
    private static final String TAG = AEActivityUtilAPI16.class.getName();
        
    /** */
    static int getFullScreenFlags() {
        Log.d(TAG,"getFullScreenFlags()");        
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_FULLSCREEN;
    }
}
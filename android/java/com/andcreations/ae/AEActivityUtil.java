package com.andcreations.ae;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.app.Activity;

/**
 * @author Mikolaj Gucki
 */
public class AEActivityUtil {
    /** The log tag. */
    private static final String TAG = AEActivityUtil.class.getName();
        
    /** */
    private Activity activity;
    
    /** */
    public AEActivityUtil(Activity activity) {
        this.activity = activity;
    }
    
    /** */
    public void keepScreenOn() {
        activity.getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    /** */
    public void enterFullScreen() {
        Log.d(TAG,"enterFullScreen(), API level is " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 14) {
            return;
        }
        int flags = 0;
        
        if (Build.VERSION.SDK_INT >= 19) {
            flags |= AEActivityUtilAPI19.getFullScreenFlags();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            flags |= AEActivityUtilAPI16.getFullScreenFlags();
        }
        if (Build.VERSION.SDK_INT >= 14) {
            flags |= AEActivityUtilAPI14.getFullScreenFlags();
        }            
        
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        
        /* View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 14 
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE // 16
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 16
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 16  
          | View.SYSTEM_UI_FLAG_FULLSCREEN // 16
          | View.SYSTEM_UI_FLAG_IMMERSIVE // 19
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // 19  */
      
    }
}
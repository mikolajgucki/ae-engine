package com.andcreations.ae.studio.plugins.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikolaj Gucki
 */
class AndroidAppState {
    /** */
    private List<AndroidAppStateListener> listeners = new ArrayList<>();
    
    /** */
    private String androidSDKDir;
    
    /** */
    void addAndroidAppStateListener(AndroidAppStateListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public String getAndroidSDKDir() {
        return androidSDKDir;
    }
    
    /** */
    public void setAndroidSDKDir(String androidSDKDir) {
        String oldSDKDir = this.androidSDKDir;
        String newSDKDir = androidSDKDir;
        this.androidSDKDir = androidSDKDir;
        
        if (StringUtils.equals(oldSDKDir,newSDKDir) == false) {
        // notify listeners
            synchronized (listeners) {
                for (AndroidAppStateListener listener:listeners) {
                    listener.androidSDKDirChanged(oldSDKDir,newSDKDir);
                }
            }
        }
    }
}
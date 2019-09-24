package com.andcreations.ae.studio.plugins.android;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.project.ProjectSettings;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
public class AndroidSettings {
    /** */
    private static AndroidSettings instance;
    
    /** */
    private AndroidPluginState state;
    
    /** */
    private List<AndroidSettingsListener> listeners = new ArrayList<>();
    
    /** */
    void init(final AndroidPluginState state) {
        this.state = state;
        
    // settings page
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                ProjectSettings.get().addPage(new AndroidSettingsPage(state));
            }
        });
    }
    
    /** */
    public void addAndroidSettingsListener(AndroidSettingsListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    void notifyAndroidProjectDirChanged(String dir) {
        synchronized (listeners) {
            for (AndroidSettingsListener listener:listeners) {
                listener.androidProjectDirChanged(dir);
            }
        }
    }
    
    /** */
    void notifyAndroidPackageChanged(String androidPackage) {
        synchronized (listeners) {
            for (AndroidSettingsListener listener:listeners) {
                listener.androidPackageChanged(androidPackage);
            }
        }
    }
    
    /** */
    public String getAndroidPackage() {
        return state.getAndroidPackage();
    }
    
    /** */
    public static AndroidSettings get() {
        if (instance == null) {
            instance = new AndroidSettings();
        }
        
        return instance;
    }
}

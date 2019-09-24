package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugin.PluginState;

/**
 * @author Mikolaj Gucki
 */
public class AndroidPluginState extends PluginState {
    /** */
    private String androidProjectDir;

    /** */
    private String androidPackage;
    
    /** */
    private boolean verbose = true;
    
    /** */
    public String getAndroidProjectDir() {
        if (androidProjectDir == null) {
            return AEProject.ANDROID_PROJECT_RELATIVE_PATH;
        }
        
        return androidProjectDir;
    }
    
    /** */
    public void setAndroidProjectDir(String androidProjectDir) {
        this.androidProjectDir = androidProjectDir;
    }
    
    /** */
    public String getAndroidPackage() {
        return androidPackage;
    }
    
    /** */
    public void setAndroidPackage(String androidPackage) {
        this.androidPackage = androidPackage;
    }
    
    /** */
    public boolean getVerbose() {
        return verbose;
    }
    
    /** */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
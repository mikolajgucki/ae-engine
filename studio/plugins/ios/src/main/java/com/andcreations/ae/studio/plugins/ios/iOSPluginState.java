package com.andcreations.ae.studio.plugins.ios;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugin.PluginState;

/**
 * @author Mikolaj Gucki
 */
public class iOSPluginState extends PluginState {
    /** */
    private String iosProjectDir;
    
    /** */
    private boolean verbose = true;
    
    /** */
    public String getiOSProjectDir() {
        if (iosProjectDir == null) {
            return AEProject.IOS_PROJECT_RELATIVE_PATH;
        }
        
        return iosProjectDir;
    }
    
    /** */
    public void setiOSProjectDir(String iosProjectDir) {
        this.iosProjectDir = iosProjectDir;
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
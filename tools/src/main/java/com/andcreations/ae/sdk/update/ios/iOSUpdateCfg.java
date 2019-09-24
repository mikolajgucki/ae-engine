package com.andcreations.ae.sdk.update.ios;

import java.io.File;

import com.andcreations.ae.sdk.update.SDKUpdateCfg;

/**
 * @author Mikolaj Gucki
 */
public class iOSUpdateCfg extends SDKUpdateCfg {
    /** */
    private File iosProjectDir;
    
    /** */
    private File appIconSetDir;
    
    /** */
    public iOSUpdateCfg(File aeDistDir,File projectDir,File iosProjectDir) {
        super(aeDistDir,projectDir);
        this.iosProjectDir = iosProjectDir;
    }
    
    /** */
    public File getiOSProjectDir() {
        return iosProjectDir;
    }
    
    /** */
    public void setAppIconSetDir(File appIconSetDir) {
        this.appIconSetDir = appIconSetDir;        
    }
    
    /** */
    public File getAppIconSetDir() {
        return appIconSetDir;
    }
}
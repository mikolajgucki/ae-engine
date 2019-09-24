package com.andcreations.ae.sdk.update.android;

import java.io.File;

import com.andcreations.ae.sdk.update.SDKUpdateCfg;

/**
 * @author Mikolaj Gucki
 */
public class AndroidUpdateCfg extends SDKUpdateCfg {
    /** */
    private File androidProjectDir;
    
    /** */
    public AndroidUpdateCfg(File aeDistDir,File projectDir,
        File androidProjectDir) {
    //
        super(aeDistDir,projectDir);
        this.androidProjectDir = androidProjectDir;
    }
    
    /** */
    public File getAndroidProjectDir() {
        return androidProjectDir;
    }
}
package com.andcreations.ae.sdk.update;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public abstract class SDKUpdateCfg {
    /** */
    private File aeDistDir;
    
    /** */
    private File projectDir;
    
    /** */
    protected SDKUpdateCfg(File aeDistDir,File projectDir) {
        this.aeDistDir = aeDistDir;
        this.projectDir = projectDir;
    }
    
    /** */
    public File getAEDistDir() {
        return aeDistDir;
    }
    
    /** */
    public File getProjectDir() {
        return projectDir;
    }
}
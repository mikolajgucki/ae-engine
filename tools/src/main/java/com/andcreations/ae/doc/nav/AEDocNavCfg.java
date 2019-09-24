package com.andcreations.ae.doc.nav;

import java.io.File;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class AEDocNavCfg {
    /** */
    private List<File> navFiles;
    
    /** */
    private File dstDir;
    
    /** */
    public AEDocNavCfg(List<File> navFiles,File dstDir) {
        this.navFiles = navFiles;
        this.dstDir = dstDir;
    }
    
    /** */
    public List<File> getNavFiles() {
        return navFiles;
    }
    
    /** */
    public File getDstDir() {
        return dstDir;
    }
}
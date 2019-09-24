package com.andcreations.ae.doc;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class AEDocCfg {
    /** */
    private List<File> roots;
    
    /** */
    private File dstDir;
    
    /** */
    public AEDocCfg(List<File> roots,File dstDir) {
        this.roots = roots;
        this.dstDir = dstDir;
    }
    
    /** */
    public AEDocCfg(File[] roots,File dstDir) {
        this(Arrays.asList(roots),dstDir);
    }
    
    /** */
    List<File> getRoots() {
        return roots;
    }
    
    /** */
    File getDstDir() {
        return dstDir;
    }
}
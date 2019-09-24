package com.andcreations.ae.eclipse;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class AEProjectsBuilderCfg {
    /** The directory with AE Engine sources. */
    private File aeSrcDir;
    
    /** The Ant home. */
    private File antHome;
    
    /**
     * Constructs a {@link AEProjectBuilderCfg}.
     *
     * @param aeSrcDir The directory with AE Engine sources.
     * @param antHome The Ant home.
     */
    public AEProjectsBuilderCfg(File aeSrcDir,File antHome) {
        this.aeSrcDir = aeSrcDir;
        this.antHome = antHome;
    }
    
    /**
     * Gets the directory with the AE Engine sources.
     *
     * @return The AE Engine sources directory.
     */
    public File getAESrcDir() {
        return aeSrcDir;
    }
    
    /**
     * Gets the Ant home.
     *
     * @return The Ant home.
     */
    public File getAntHome() {
        return antHome;
    }
}
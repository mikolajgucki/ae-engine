package com.andcreations.ae.tex.pack;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface TexPackerListener {
    /** */
    void texPackDirCreated(File texPackFile,File dir);
    
    /** */
    void texPackFileCreated(File texPackFile,File file);
}
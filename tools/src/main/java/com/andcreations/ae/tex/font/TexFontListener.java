package com.andcreations.ae.tex.font;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface TexFontListener {
    /** */
    void texFontDirCreated(File texFontFile,File dir);
    
    /** */
    void texFontFileCreated(File texFontFile,File file);    
}
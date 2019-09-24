package com.andcreations.ae.tex.font.ext;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class TexFontExtCfg {
    /** */
    private File imageFile;
    
    /** */
    private File dataFile;
    
    /** */
    private File dstDir;
    
    /** */
    public TexFontExtCfg(File imageFile,File dataFile,File dstDir) {
        this.imageFile = imageFile;
        this.dataFile = dataFile;
        this.dstDir = dstDir;
    }
    
    /** */
    File getImageFile() {
        return imageFile;
    }
    
    /** */
    File getDataFile() {
        return dataFile;
    }
    
    /** */
    File getDstDir() {
        return dstDir;
    }
}
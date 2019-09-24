package com.andcreations.ae.sdk.update;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface SDKUpdaterListener {
    /** */
    void log(String message);
    
    /** */
    void copying(File srcFile,File dstFile);
    
    /** */
    void deleting(File file);
    
    /** */
    void skipping(File srcFile,File dstFile);
    
    /** */
    void updatingIcon(File file);
}
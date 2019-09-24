package com.andcreations.ae.sdk.update;

import java.io.File;

/**
 * The interface for the directory sync.
 *
 * @author Mikolaj Gucki
 */
public interface DirSyncListener {
    /** */
    void dirSyncCopying(File srcFile,File dstFile);
    
    /** */
    void dirSyncDeleting(File file);
}
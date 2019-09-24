package com.andcreations.ae.project;

import java.io.File;

/**
 * Project cleaner listener.
 *
 * @author Mikolaj Gucki
 */ 
public interface AEProjectCleanerListener {
    /**
     * Called when a directory has been deleted.
     *
     * @param dir The directory.
     */
    void dirDeleted(File dir);
}

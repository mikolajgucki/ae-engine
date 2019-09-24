package com.andcreations.ae.studio.plugins.file;

import java.io.File;
import java.util.List;

/**
 * The interface for file listeners.
 *
 * @author Mikolaj Gucki
 */
public interface FileListener {
    /**
     * Called when a file has been created.
     *
     * @param file The file.
     */
    void fileCreated(File file);    
    
    /**
     * Called when the file content has changed.
     *
     * @param file The file.
     */
    void fileChanged(File file);
    
    /**
     * Called when the file issue list has changed.
     *
     * @param file The file.
     * @param issues The issues list.
     */
    void fileIssuesChanged(File file,List<FileIssue> issue);
    
    /**
     * Called when a file has been deleted.
     *
     * @param file The file.
     */
    void fileDeleted(File file);      
    
    /**
     * Called when a directory has been created.
     *
     * @param dir The directory.
     */
    void dirCreated(File dir);      
    
    /**
     * Called when a directory has been deleted.
     *
     * @param dir The directory.
     */
    void dirDeleted(File dir);     
    
    /**
     * Called when a file has been renamed.
     *
     * @param src The source file.
     * @param dst The destination file.
     */
    void fileRenamed(File src,File dst);
}

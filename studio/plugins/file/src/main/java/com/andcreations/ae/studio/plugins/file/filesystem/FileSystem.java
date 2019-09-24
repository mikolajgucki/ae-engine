package com.andcreations.ae.studio.plugins.file.filesystem;

/**
 * The interface for a file system.
 *
 * @author Mikolaj Gucki
 */
public interface FileSystem {
    /** The name-separator character. */
    public static final char SEPARATOR = '/';
    
    /**
     * Lists the root directories of the file system.
     *
     * @return The roots.
     */
    FSFile[] listRoots();
    
    /**
     * Lists files in a directory.
     *
     * @param dir The directory.
     */
    FSFile[] list(FSFile dir);
}
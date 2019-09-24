package com.andcreations.ae.studio.plugins.file.filesystem;

/**
 * A file system file.
 *
 * @author Mikolaj Gucki
 */
public interface FSFile {
    /**
     * Gets the file name.
     *
     * @return The file name.
     */
    String getName();

    /**
     * Indicates if the file is a regular file.
     *
     * @return <code>true</code> if the file is a regular file,
     *   <code>false</code> otherwise.
     */
    boolean isFile();
    
    /**
     * Indicates if the file is a directory.
     *
     * @return <code>true</code> if the file is a directory, <code>false</code>
     *   otherwise.
     */
    boolean isDir();
}
package com.andcreations.ant;

import java.io.File;

/**
 * Represents a nested element of an Ant task with a path attribute.
 * 
 * @author Mikolaj Gucki
 */
public class AntPath {
    /** The path attribute value. */
    private String path;
    
    /**
     * Sets the path attribute value.
     * 
     * @param path The path attribute value.
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    /**
     * Gets the path.
     * 
     * @return The path.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Gets the file denoted by the path.
     *
     * @return The file.
     */
    public File getFile() {
        return new File(path);
    }
}

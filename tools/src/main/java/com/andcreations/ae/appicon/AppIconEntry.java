package com.andcreations.ae.appicon;

/**
 * @author Mikolaj Gucki
 */
public class AppIconEntry {
    /** The destination file name. */
    private String filename;    
    
    /** The destination width. */
    private int width;
    
    /** The destination height. */
    private int height;
    
    /** */
    AppIconEntry() {
    }
    
    /**
     * Gets the destination file name.
     *
     * @return The file name.
     */
    public String getFilename() {
        return filename;
    }
    
    /**
     * Gets the destination width.
     *
     * @return The destination width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the destination height.
     *
     * @return The destination height.
     */
    public int getHeight() {
        return height;
    }
}
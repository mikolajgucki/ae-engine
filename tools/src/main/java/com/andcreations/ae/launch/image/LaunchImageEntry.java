package com.andcreations.ae.launch.image;

/**
 * @author Mikolaj Gucki
 */
public class LaunchImageEntry {
    /** */
    private String name;
    
    /** */
    private String filename;
    
    /** */
    private int width;
    
    /** */
    private int height;
    
    /** */
    private LaunchImageOrientation orientation;
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    public String getFilename() {
        return filename;
    }
    
    /** */
    public int getWidth() {
        return width;
    }
    
    /** */
    public int getHeight() {
        return height;
    }
    
    /** */
    public LaunchImageOrientation getOrientation() {
        return orientation;
    }
}
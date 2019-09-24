package com.andcreations.system;

/**
 * Determines the operating system.
 *
 * @author Mikolaj Gucki
 */
public class OS {
    /** The poperty value for unknown OS. */
    public static final String UNKNOWN = "unknown";
    
    /** The poperty value for Windows. */
    public static final String WINDOWS = "windows";
    
    /** The poperty value for Linux. */
    public static final String LINUX = "linux";
    
    /** The property for OS X. */
    public static final String OSX = "osx";

    /** */
    public static String getOS() {
        String name = System.getProperty("os.name").toLowerCase();
        
        if (name.contains("win")) {
            return WINDOWS;
        }
        if (name.contains("inux")) {
            return LINUX;
        }
        if (name.contains("mac")) {
            return OSX;
        }
        
        return UNKNOWN;
    }
}
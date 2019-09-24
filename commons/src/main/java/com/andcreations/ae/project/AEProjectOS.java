package com.andcreations.ae.project;

/**
 * The OS under which an app (project) can be simulated to run.
 *
 * @author Mikolaj Gucki
 */
public enum AEProjectOS {
    /** */
    ANDROID("android"),
    
    /** */
    IOS("ios");
    
    /** */
    private String osName;
    
    /** */
    private AEProjectOS(String osName) {
        this.osName = osName;
    }
    
    /** */
    public String getOSName() {
        return osName;
    }
    
    /** */
    public static boolean isValid(String osName) {
        for (AEProjectOS os:AEProjectOS.values()) {
            if (os.getOSName().equals(osName)) {
                return true;
            }
        }
        
        return false;
    }
}
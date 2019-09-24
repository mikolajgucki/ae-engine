package com.andcreations.android;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class AndroidProject {
    /** */
    public static final String MANIFSET_FILENAME = "src/main/AndroidManifest.xml";
    
    /**    
     * Checks if a director denotes an Android project directory.
     *
     * @param dir The directory.
     * @return <code>true</code> if the directory is an Android project
     *   directory, <code>false</code> otherwise.
     */
    public static boolean isAndroidProjectDir(File dir) {
        File androidManifestFile = new File(dir,MANIFSET_FILENAME);
        if (androidManifestFile.exists() == true &&
            androidManifestFile.isFile() == true) {
        //
            return true;
        }
        
        return false;
    }
}
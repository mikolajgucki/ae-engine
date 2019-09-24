package com.andcreations.ios;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Mikolaj Gucki
 */
public class iOSProject {
    /** */
    public static final String XCODE_PROJECT_SUFFIX = ".xcodeproj";
    
    /** */
    public static final String AE_BUILD_PROPERTIES_FILE = "build-ae.properties";
    
    /** */
    public static final String AE_APP_ICON_SET_DIR = "ae.app.icon.set.dir";
    
    /**    
     * Checks if a director denotes an iOS project directory.
     *
     * @param dir The directory.
     * @return <code>true</code> if the directory is an iOS project
     *   directory, <code>false</code> otherwise.
     */
    public static boolean isiOSProjectDir(File dir) {
        File[] files = dir.listFiles();
        for (File file:files) {
            if (file.isDirectory() == true &&
                file.getName().endsWith(XCODE_PROJECT_SUFFIX) == true) {
            //
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    public static File getAEBuildPropertiesFile(File iosProjectDir) {
        return new File(iosProjectDir,AE_BUILD_PROPERTIES_FILE);
    }
    
    /** */
    public static Properties loadAEBuildProperties(File iosProjectDir)
        throws IOException {
    //
        Properties props = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(
                getAEBuildPropertiesFile(iosProjectDir));
            props.load(stream);
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException exception) {
                }
            }
        }
        
        return props;
    }
}
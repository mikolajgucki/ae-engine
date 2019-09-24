package com.andcreations.ae.ios;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Mikolaj Gucki
 */
public class AEiOSProject {
    /** */
    public static final String AE_BUILD_PROPERTIES_FILE = "build-ae.properties";
    
    /** */
    public static final String AE_APP_ICON_SET_DIR = "ae.app.icon.set.dir";
    
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
    
    /** */
    public static String getAppIconSetDir(Properties properties) {
        return properties.getProperty(AE_APP_ICON_SET_DIR);
    }
}
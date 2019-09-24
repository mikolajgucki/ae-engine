package com.andcreations.ae.studio.plugins.android;

import java.io.File;

import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.android.sdk.AndroidSDK;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AndroidSDKDir {
    /** */
    private static BundleResources res =
        new BundleResources(AndroidSDKDir.class); 
        
    /** */
    public static String validate(File dir) {
    // must exist
        if (dir.exists() == false) {
            return res.getStr("not.found");
        }
        
    // must be a directory
        if (dir.isDirectory() == false) {
            return res.getStr("not.dir");
        }
        
    // must be an Android SDK
        if (AndroidSDK.isAndroidSDKDir(dir) == false) {
            return res.getStr("not.sdk.project");
        }        
        
        return null;
    }
    
    /** */
    public static File fromPath(String path) {
        File dir = new File(path);
        if (dir.isAbsolute() == false) {
            dir = ProjectFiles.get().getFileFromRelativePath(path);
        }

        return dir;        
    }
}
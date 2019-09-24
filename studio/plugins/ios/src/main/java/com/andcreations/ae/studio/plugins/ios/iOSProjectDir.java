package com.andcreations.ae.studio.plugins.ios;

import java.io.File;

import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ios.iOSProject;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class iOSProjectDir {
    /** */
    private static BundleResources res =
        new BundleResources(iOSProjectDir.class); 
        
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
        
    // must be an iOS project
        if (iOSProject.isiOSProjectDir(dir) == false) {
            return res.getStr("not.ios.project");
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
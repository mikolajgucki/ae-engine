package com.andcreations.ae.studio.plugins.android;

import java.io.File;

import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.android.AndroidProject;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AndroidProjectDir {
    /** */
    private static BundleResources res =
        new BundleResources(AndroidProjectDir.class); 
        
    /** */
    private static AndroidProjectDir instance;
    
    /** */
    private AndroidPluginState state;
        
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
        
    // must be an Android project
        if (AndroidProject.isAndroidProjectDir(dir) == false) {
            return res.getStr("not.android.project");
        }        
        
        return null;
    }
    
    /** */
    public static boolean isValid(File dir) {
        return validate(dir) == null;
    }
    
    /** */
    public static File fromPath(String path) {
        File dir = new File(path);
        if (dir.isAbsolute() == false) {
            dir = ProjectFiles.get().getFileFromRelativePath(path);
        }

        return dir;        
    }
    
    /** */
    public File getAndroidProjectDir() {
        return fromPath(state.getAndroidProjectDir());
    }
    
    /**
     * Checks if the Android project directory is set and is valid.
     *
     * @return The directory or <code>null</code> if the directory is not set
     *   or is not valid.
     */
    public File tryToGet() {
    // directory
        String path = state.getAndroidProjectDir();
        if (path == null || path.length() == 0) {
            CommonDialogs.error(res.getStr("dir.chk.title"),
                res.getStr("dir.not.set"));
            return null;
        }
        File androidProjectDir = AndroidProjectDir.fromPath(path);
        
    // validate
        String error = AndroidProjectDir.validate(androidProjectDir);
        if (error != null) {
            CommonDialogs.error(res.getStr("dir.chk.title"),
                res.getStr("dir.error",error));
            return null;
        }   
        
        return androidProjectDir;
    }
    
    /** */
    void init(AndroidPluginState state) {
        this.state = state;
    }
    
    /** */
    public static AndroidProjectDir get() {
        if (instance == null) {
            instance = new AndroidProjectDir();
        }
        
        return instance;
    }
}
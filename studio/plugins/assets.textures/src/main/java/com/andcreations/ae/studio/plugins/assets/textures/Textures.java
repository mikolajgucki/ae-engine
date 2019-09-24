package com.andcreations.ae.studio.plugins.assets.textures;

import java.io.File;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;

/**
 * @author Mikolaj Gucki
 */
public class Textures {
    /** */
    private static Textures instance;
    
    /** */
    public File getTexturesDir() {
        return ProjectFiles.get().getFileFromRelativePath(
            AEProject.TEXTURES_PATH);
    }
    
    /** */
    public static Textures get() {
        if (instance == null) {
            instance = new Textures();
        }
        
        return instance;
    }
}
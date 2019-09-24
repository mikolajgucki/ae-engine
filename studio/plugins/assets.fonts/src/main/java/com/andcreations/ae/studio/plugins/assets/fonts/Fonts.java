package com.andcreations.ae.studio.plugins.assets.fonts;

import java.io.File;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;

/**
 * @author Mikolaj Gucki
 */
public class Fonts {
    /** */
    private static Fonts instance;
    
    /** */
    public File getFontsDir() {
        return ProjectFiles.get().getFileFromRelativePath(AEProject.FONTS_PATH);        
    }
    
    /** */
    public static Fonts get() {
        if (instance == null) {
            instance = new Fonts();
        }
        
        return instance;
    }
}
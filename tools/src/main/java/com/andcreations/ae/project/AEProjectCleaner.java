package com.andcreations.ae.project;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Cleans a project.
 *
 * @author Mikolaj Gucki
 */ 
public class AEProjectCleaner {
    /** */
    private AEProjectCleanerListener listener;
    
    /** */
    public AEProjectCleaner(AEProjectCleanerListener listener) {
        this.listener = listener;
    }
    
    /** */
    public AEProjectCleaner() {
        this(null);
    }
    
    /** */
    public void cleanProject(File projectDir) throws IOException {
        File buildDir = new File(projectDir,AEProject.BUILD_PATH);
        FileUtils.deleteDirectory(buildDir);
        
        if (listener != null) {
            
        }
    }
}
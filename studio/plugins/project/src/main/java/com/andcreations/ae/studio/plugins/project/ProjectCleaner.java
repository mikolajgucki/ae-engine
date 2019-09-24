package com.andcreations.ae.studio.plugins.project;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class ProjectCleaner {
    /** */
    private static final BundleResources res =
        new BundleResources(ProjectCleaner.class); 
    
    /** */
    static void cleanProject() {
        File buildDir = new File(ProjectFiles.get().getProjectDir(),
            AEProject.BUILD_PATH);
        
    // delete the build directory
        try {
            Files.get().deleteDir(buildDir);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("clean.failed.title"),
                res.getStr("clean.failed.msg",exception.getMessage()));
        }
     
    // log
        DefaultConsole.get().println(res.getStr("dir.deleted",
            buildDir.getAbsolutePath()));
    }
}
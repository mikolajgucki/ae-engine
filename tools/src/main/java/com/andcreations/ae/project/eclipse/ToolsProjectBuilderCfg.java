package com.andcreations.ae.project.eclipse;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class ToolsProjectBuilderCfg {
    /** The project directory. */
    private File projectDir;
    
    /** The project name. */
    private String projectName;
    
    /** The AE Engine distribution directory. */
    private File aeDistDir;
    
    /** The Ant home. */
    private File antHome;

    /** */
    public ToolsProjectBuilderCfg(File projectDir,String projectName,
        File aeDistDir,File antHome) {
    //
        this.projectDir = projectDir;
        this.projectName = projectName;
        this.aeDistDir = aeDistDir;
        this.antHome = antHome;
    }

    /** */
    public File getProjectDir() {
        return projectDir;
    }
    
    /** */
    public String getProjectName() {
        return projectName;
    }

    /** */
    public File getAEDistDir() {
        return aeDistDir;
    }
    
    /** */
    public File getAntHome() {
        return antHome;
    }
}

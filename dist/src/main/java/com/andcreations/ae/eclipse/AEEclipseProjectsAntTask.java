package com.andcreations.ae.eclipse;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;

/**
 * The Ant task which creates the Eclipse projects from the AE Java projects.
 * 
 * @author Mikolaj Gucki
 */
public class AEEclipseProjectsAntTask extends AETask {
    /** The AE sources directory. */
    private AntPath aeSrcDir;

    /** The destination directory. */
    private AntPath dstDir;
    
    /** The Ant home directory. */
    private AntPath antHomeDir;

    /** */
    public AntPath createAESrcDir() {
        if (aeSrcDir != null) {
            duplicatedElement("aeSrcDir");
        }
        
        aeSrcDir = new AntPath();
        return aeSrcDir;
    }  
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstDir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }  
    
    /** */
    public AntPath createAntHomeDir() {
        if (antHomeDir != null) {
            duplicatedElement("antHomeDir");
        }
        
        antHomeDir = new AntPath();
        return antHomeDir;
    }  
    
    /** */
    @Override
    public void execute() throws BuildException {
        verifyElementSet(aeSrcDir,"aeSrcDir");     
        verifyElementSet(dstDir,"dstDir");
        verifyElementSet(antHomeDir,"antHomeDir");
        
    // log
        log(String.format("Creating Eclipse projects in %s",
            dstDir.getFile().getAbsolutePath()));
        
    // create projects
        AEProjectsBuilderCfg cfg = new AEProjectsBuilderCfg(
            aeSrcDir.getFile(),antHomeDir.getFile());
        AEProjectsBuilder builder = new AEProjectsBuilder(cfg);
        try {
            builder.createProjects(dstDir.getFile());
        } catch (IOException exception) {
            throw new BuildException(exception);
        }        
    }
}

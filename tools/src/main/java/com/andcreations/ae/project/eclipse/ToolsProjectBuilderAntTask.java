package com.andcreations.ae.project.eclipse;

import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.AntSingleValue;

/**
 * Creates an Eclipse project for AE project tools.
 * 
 * @author Mikolaj Gucki
 */
public class ToolsProjectBuilderAntTask extends AETask {
    /** The project directory. */
    private AntPath projectDir;
    
    /** The project name. */
    private AntSingleValue projectName;
    
    /** The AE Engine distribution directory. */
    private AntPath aeDistDir;
    
    /** The Ant home. */
    private AntPath antHomeDir;
    
    /** The destination directory. */
    private AntPath dstDir;    
    
    /** */
    public AntPath createProjectDir() {
    	if (projectDir != null) {
    		duplicatedElement("projectDir");
    	}
    	
    	projectDir = new AntPath();
    	return projectDir;
    }
    
    /** */
    public AntSingleValue createProjectName() {
    	if (projectName != null) {
    		duplicatedElement("projectName");
    	}
    	
    	projectName = new AntSingleValue();
    	return projectName;    	
    }
    
    /** */
    public AntPath createAEDistDir() {
    	if (aeDistDir != null) {
    		duplicatedElement("aeDistDir");
    	}
    	
    	aeDistDir = new AntPath();
    	return aeDistDir;    	
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
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstDir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }  
    
    /** */
    @Override
    public void execute() throws BuildException {
        verifyElementSet(projectDir,"projectDir");     
        verifyElementSet(projectName,"projectName");
        verifyElementSet(aeDistDir,"aeDistDir");
        verifyElementSet(antHomeDir,"antHomeDir");
        verifyElementSet(dstDir,"dstDir");
        
    // log
        log(String.format("Creating Eclipse project in %s",
            dstDir.getFile().getAbsolutePath()));
        
    // create
        ToolsProjectBuilderCfg cfg = new ToolsProjectBuilderCfg(
            projectDir.getFile(),projectName.getValue(),aeDistDir.getFile(),
            antHomeDir.getFile());
        try {
        	ToolsProjectBuilder builder = new ToolsProjectBuilder(cfg);
        	builder.createProject(dstDir.getFile());
        } catch (IOException exception) {
        	throw new BuildException(exception);
        }
    }
}

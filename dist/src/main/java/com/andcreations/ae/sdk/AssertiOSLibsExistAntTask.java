package com.andcreations.ae.sdk;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Asserts iOS libraries exist for given configuration and iOS SDK.
 *
 * @author Mikolaj Gucki
 */
public class AssertiOSLibsExistAntTask extends AETask {
    /** The string resources. */
    private StrResources res = 
        new BundleResources(AssertiOSLibsExistAntTask.class);      
    
    /** The path to the directory with iOS libraries. */
    private AntPath libsDir;
    
    /** */
    public AntPath createLibsDir() {
        if (libsDir != null) {
            duplicatedElement("libsdir");
        }
        
        libsDir = new AntPath();
        return libsDir;
    }
     
    /** */
    private void assertLibsExist() {
    // check if the directory exists
        File dir = new File(libsDir.getPath());
        if (dir.exists() == false || dir.isDirectory() == false) {
            throw new BuildException(res.getStr("dir.not.found",
                dir.getAbsolutePath()));
        }
        
    // check if the directory contains files
        boolean containsFiles = false;
        File[] files = dir.listFiles();
        for (File file:files) {
            if (file.isFile() == true) {
                containsFiles = true;
                break;                
            }
        }
        if (containsFiles == false) {
            throw new BuildException(res.getStr("no.files.found",
                dir.getAbsolutePath()));
        }
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(libsDir,"libsdir");
        assertLibsExist();
    }        
}   
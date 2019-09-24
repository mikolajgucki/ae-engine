package com.andcreations.ae.doc;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;

/**
 * @author Mikolaj Gucki
 */
public class AEDocAntTask extends AETask {
    /** The source directory. */
    private AntPath srcDir;
    
    /** The destination directory. */
    private AntPath dstDir;
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstDir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }    
    
    /** */
    public AntPath createSrcDir() {
        if (srcDir != null) {
            duplicatedElement("srcDir");
        }
        
        srcDir = new AntPath();
        return srcDir;
    }    
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(srcDir,"srcDir");
        verifyElementSet(dstDir,"dstDir");
        
        File srcDirFile = new File(srcDir.getPath());
        verbose("Source directory is " + srcDirFile.getAbsolutePath());
        
        File dstDirFile = new File(dstDir.getPath());
        verbose("Destination directory is " + dstDirFile.getAbsolutePath());
        
        AEDocCfg cfg = new AEDocCfg(new File[]{srcDirFile},dstDirFile);
        AEDoc doc = new AEDoc(cfg);
        try {
            doc.process();
        } catch (IOException exception) {
            throw new BuildException(exception.getMessage());
        }
    }
}
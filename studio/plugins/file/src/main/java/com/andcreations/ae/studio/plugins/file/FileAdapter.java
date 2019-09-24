package com.andcreations.ae.studio.plugins.file;

import java.io.File;
import java.util.List;

/**
 * The abstract file listener which does nothing.
 *
 * @author Mikolaj Gucki
 */
public abstract class FileAdapter implements FileListener {
    /** */
    @Override
    public void fileCreated(File file) {
    }
    
    /** */
    @Override
    public void fileChanged(File file) {
    }
    
    /** */
    @Override    
    public void fileIssuesChanged(File file,List<FileIssue> issues) {
    }
    
    /** */
    @Override
    public void fileDeleted(File file) {
    }
    
    /** */
    @Override
    public void dirCreated(File dir) {
    }
    
    /** */
    @Override
    public void dirDeleted(File dir) {
    }
    
    /** */
    @Override
    public void fileRenamed(File src,File dst) {
    }
}

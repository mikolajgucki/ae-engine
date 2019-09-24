package com.andcreations.ae.doc;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class AEDocFile {
    /** */
    private File root;
    
    /** */
    private File file;
    
    /** */
    private String path;
    
    /** */
    AEDocFile(File root,File file,String path) {
        this.root = root;
        this.file = file;
        this.path = path;
    }
    
    /** */
    public File getRoot() {
        return root;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public String getPath() {
        return path;
    }
}
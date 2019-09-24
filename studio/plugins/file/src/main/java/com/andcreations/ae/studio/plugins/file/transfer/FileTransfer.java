package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class FileTransfer {
    /** */
    private static FileTransfer instance;
    
    /** */
    private FileMove fileMove = new FileMove();
    
    /** */
    private FileCopy fileCopy = new FileCopy();
    
    /** */
    public void move(List<File> files,File dstDir) {
        fileMove.move(files,dstDir);
    }
    
    /** */
    public void copy(List<File> files,File dstDir) {
        fileCopy.copy(files,dstDir);
    }
    
    /** */
    public static FileTransfer get() {
        if (instance == null) {
            instance = new FileTransfer();
        }
        
        return instance;
    }
}
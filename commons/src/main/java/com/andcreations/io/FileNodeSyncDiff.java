package com.andcreations.io;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class FileNodeSyncDiff {
    /** */
    public static enum Type {
        /** */
        ADDED,
        
        /** */
        DELETED;
    }
    
    /** */
    private Type type;
    
    /** */
    private File file;
    
    /** */
    private String path;
    
    /** */
    FileNodeSyncDiff(File file,Type type) {
        this.file = file;
        this.type = type;
    }
    
    /** */
    FileNodeSyncDiff(File file,Type type,String path) {
        this(file,type);
        this.path = path;
    }
    
    /** */
    public Type getType() {
        return type;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public String getPath() {
        return path;
    }
   
    /** */
    static FileNodeSyncDiff added(File file) {        
        return new FileNodeSyncDiff(file,Type.ADDED);
    }
   
    /** */
    static FileNodeSyncDiff added(File file,String path) {        
        return new FileNodeSyncDiff(file,Type.ADDED,path);
    }
   
    /** */
    static FileNodeSyncDiff deleted(File file) {        
        return new FileNodeSyncDiff(file,Type.DELETED);
    }
   
    /** */
    static FileNodeSyncDiff deleted(File file,String path) {        
        return new FileNodeSyncDiff(file,Type.DELETED,path);
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s[%s]:%s",type,path != null ? path : "",
            file.getAbsolutePath());
    }
}
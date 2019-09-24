package com.andcreations.ae.studio.plugins.file.filesystem;

import java.io.File;

/**
 * A file system file.
 *
 * @author Mikolaj Gucki
 */
class DiskFSFile implements FSFile {
    /** The file */
    private File file;
    
    /** */
    DiskFSFile(File file) {
        this.file = file;
    }
    
    /** */
    File getFile() {
        return file;
    }
    
    /** */
    @Override
    public String getName() {
        return file.getName();
    }
    
    /** */
    @Override
    public boolean isFile() {
        return file.isFile();
    }
    
    /** */
    @Override
    public boolean isDir() {
        return file.isDirectory();
    }
}
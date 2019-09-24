package com.andcreations.ae.studio.plugins.file.filesystem;

import java.io.File;

/**
 * The file system implementation operating on the local disks.
 *
 * @author Mikolaj Gucki
 */
public class DiskFileSystem implements FileSystem {
    /** */
    @Override
    public FSFile[] listRoots() {
        File[] roots = File.listRoots();
        FSFile[] fsRoots = new FSFile[roots.length];
        
    // convert
        for (int index = 0; index < roots.length; index++) {
            fsRoots[index] = new DiskFSFile(roots[index]);
        }
        
        return fsRoots;
    }
    
    /** */
    @Override
    public FSFile[] list(FSFile dir) {
        DiskFSFile diskFSDir = (DiskFSFile)dir;
        
        File[] files = diskFSDir.getFile().listFiles();
        FSFile[] fsFiles = new FSFile[files.length];
        
    // convert
        for (int index = 0; index < files.length; index++) {
            fsFiles[index] = new DiskFSFile(files[index]);
        }
        
        return fsFiles;
    }
}
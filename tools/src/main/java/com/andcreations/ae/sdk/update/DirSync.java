package com.andcreations.ae.sdk.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.io.FileNodeSync;
import com.andcreations.io.FileNodeSyncDiff;
import com.andcreations.io.FileTree;

/**
 * Synchronizes two directories.
 *
 * @author Mikolaj Gucki
 */
public class DirSync {
    /** */
    private File srcDir;
    
    /** */
    private File dstDir;
    
    /** */
    private DirSyncListener listener;
    
    /** */
    public DirSync(File srcDir,File dstDir,DirSyncListener listener) {
        this.srcDir = srcDir;
        this.dstDir = dstDir;
        this.listener = listener;
    }
    
    /** */
    private void copy(File srcFile,File dstFile) throws IOException {
        listener.dirSyncCopying(srcFile,dstFile);
        if (srcFile.isDirectory() == true) {
            FileUtils.copyDirectory(srcFile,dstFile);
        }
        else {
            FileUtils.copyFile(srcFile,dstFile);
        }
    }
    
    /** */
    private void delete(File file) throws IOException {
        listener.dirSyncDeleting(file);
        FileUtils.forceDelete(file);
    }
    
    /** */
    public void sync() throws IOException {
        FileTree srcTree = FileTree.build(srcDir,null);
        FileTree dstTree = FileTree.build(dstDir,null);
        
    // synchronize
        List<FileNodeSyncDiff> diffs = new ArrayList<>();
        FileNodeSync.sync(srcTree.getRoot(),dstTree.getRoot(),diffs);
        
    // perform operations
        for (FileNodeSyncDiff diff:diffs) {
            if (diff.getType() == FileNodeSyncDiff.Type.ADDED) {
                File srcFile = new File(srcTree.getRoot().getFile(),
                    diff.getPath());
                File dstFile = new File(dstTree.getRoot().getFile(),
                    diff.getPath());
                copy(srcFile,dstFile);
            }
            if (diff.getType() == FileNodeSyncDiff.Type.DELETED) {
                File dstFile = new File(dstTree.getRoot().getFile(),
                    diff.getPath());
                delete(dstFile);
            }
        }
    }
}
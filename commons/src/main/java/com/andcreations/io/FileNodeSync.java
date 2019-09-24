package com.andcreations.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.andcreations.io.FileNodeSyncDiff.Type;

/**
 * @author Mikolaj Gucki
 */
public class FileNodeSync {
    /** */
    private static void buildTree(FileNodeSyncDiff root,
        List<FileNodeSyncDiff> diffs) {
    //
        DirScanner scanner = new DirScanner(root.getFile());
        String[] paths = scanner.build();
        
        for (String path:paths) {
            diffs.add(FileNodeSyncDiff.added(new File(root.getFile(),path)));
        }
    }
    
    /** */
    public static void buildFullTree(List<FileNodeSyncDiff> diffs) {
        List<FileNodeSyncDiff> newDiffs = new ArrayList<>();
        for (FileNodeSyncDiff diff:diffs) {
            buildTree(diff,newDiffs);
            
        }
        diffs.addAll(newDiffs);
    }
    
    /** */
    public static void sort(List<FileNodeSyncDiff> diffs) {
        Collections.sort(diffs,new Comparator<FileNodeSyncDiff>() {
            /** */
            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
            
            /** */
            @Override
            public int compare(FileNodeSyncDiff a,FileNodeSyncDiff b) {
                Type ta = a.getType();
                Type tb = b.getType();
                
            // deleted first
                if (ta == Type.DELETED && tb == Type.ADDED) {
                    return -1;
                }
                if (ta == Type.ADDED && tb == Type.DELETED) {
                    return 1;
                }
                
            // by path length ascending if added
                if (ta == Type.ADDED) {
                    return a.getFile().getAbsolutePath().length() -
                        b.getFile().getAbsolutePath().length();
                }                
                
            // by path length descending if deleted
                return b.getFile().getAbsolutePath().length() -
                    a.getFile().getAbsolutePath().length();
            }
        });
    }
    
    /** */
    private static String removeRootFromPath(String path,char separator) {
        int index = path.indexOf(separator);
        if (index == -1) {
            return "";
        }
        return path.substring(index + 1);
    }
    
    /** */
    public static void sync(FileNode srcNode,FileNode dstNode,
        List<FileNodeSyncDiff> diffs) {
    // find files which exist in the source node and *don't* exist in the
    // destination node
        for (FileNode srcChildNode:srcNode.getChildNodes()) {
            if (srcChildNode.getFiles().size() != 1) {
                throw new IllegalStateException("Cannot sync multi-file node");
            }
            File srcChildFile = srcChildNode.getFile();
            File dstChildFile = new File(dstNode.getFile(),
                srcChildFile.getName());
            //System.out.println("dstChildFile=" + dstChildFile);
            
            if (dstNode.hasChildNode(dstChildFile) == false) {
                String path = srcChildNode.getPath(File.separatorChar);
                path = removeRootFromPath(path,File.separatorChar);
                diffs.add(FileNodeSyncDiff.added(dstChildFile,path));
            }
            else {
                FileNode dstChildNode =
                    dstNode.getChildNodeByFile(dstChildFile);
                sync(srcChildNode,dstChildNode,diffs);
            }
        }
        
    // find files which *don't* exist in the source node and exist in the
    // destination node
        for (FileNode dstChildNode:dstNode.getChildNodes()) {
            if (dstChildNode.getFiles().size() != 1) {
                throw new IllegalStateException("Cannot sync multi-file node");
            }
            
            File dstChildFile = dstChildNode.getFile();
            File srcChildFile = new File(srcNode.getFile(),
                dstChildFile.getName());
            //System.out.println("srcChildFile=" + srcChildFile);
            
            if (srcNode.hasChildNode(srcChildFile) == false) {
                String path = dstChildNode.getPath(File.separatorChar);
                path = removeRootFromPath(path,File.separatorChar);
                diffs.add(FileNodeSyncDiff.deleted(srcChildFile,path));
            }
        }
    }
}
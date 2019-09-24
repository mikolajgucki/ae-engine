package com.andcreations.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a file tree.
 *
 * @author Mikolaj Gucki
 */
public class FileTree {
    /** The root node. */
    private FileNode root;
    
    /** The default comparator. */
    private FileNodeComparator comparator = new FileNodeComparator();
    
    /** The file-to-node map. */
    private Map<File,FileNode> fileToNodeMap = new HashMap<>();
    
    /**
     * Creates a {@link FileTree}.
     *
     * @param root The root node.
     */
    public FileTree(FileNode root) {
        this.root = root;
    }
    
    /**
     * Creates a {@link FileTree}.
     */
    public FileTree() {
        this.root = new FileNode();
    }
    
    /** */
    public FileNode getRoot() {
        return root;
    }
    
    /**
     * Merges this and another file tree. The other tree becomes invalid after
     *   the merge and must be discarded.
     *
     * @param tree The tree to merge.
     */
    public void merge(FileTree tree) {
        root.merge(tree.getRoot());
        
    // rebuild the node map
        fileToNodeMap.clear();
        root.setTree(this);
    }

    /**
     * Sorts the tree.
     *
     * @param comparator The comparator.
     */
    public void sort(Comparator<FileNode> comparator) {
        root.sortTree(comparator);
    }
    
    /**
     * Sorts the tree by name (directories before files).
     *
     * @param comparator The comparator.
     */
    public void sort() {
        root.sortTree(comparator);
    }
    
    /**
     * Gets the default comparator.
     *
     * @return The comparator.
     */
    public FileNodeComparator getDefaultComparator() {
        return comparator;
    }
    
    /**
     * Gets all children and grand children and so on nodes as list.
     *
     * @return All the (grand) children nodes as list.
     */
    public List<FileNode> flatten() {
        List<FileNode> nodes = root.flatten();
        nodes.add(root);
        return nodes;
    }  
    
    /** */
    public FileNode findNode(File file) {
        return fileToNodeMap.get(file);
    }
    
    /** */
    public FileNode findParentNode(File file) {
        File fileDir = file.getParentFile();
        List<FileNode> nodes = flatten();  
        
    // for each node
        for (FileNode node:nodes) {
            for (File nodeDir:node.getFiles()) {
                if (nodeDir.equals(fileDir) == true) {
                    return node;
                }
            }
        }
        
        return null;
    }
    
    /** */
    void addFileToNodeEntry(File file,FileNode node) {
        fileToNodeMap.put(file,node);
    }
    
    /** */
    void removeFileToNodeEntry(File file) {
        fileToNodeMap.remove(file);
    }
    
    /** */
    @Override
    public String toString() {
        return root.toString(0,false);
    }
    
    /** */
    private static FileNode buildNode(File dir,FileFilter filter) {
        FileNode parent = new FileNode(dir);
        
        File[] files = dir.listFiles(filter);
        if (files != null) {
        // for each file
            for (File file:files) {
                try {
                    file = file.getCanonicalFile();
                } catch (IOException exception) {
                }
                
                if (file.isDirectory() == true) {
                    parent.addChildNode(buildNode(file,filter));
                }
                else {
                    parent.addChildNode(new FileNode(file));
                }
            }
        }
        
        return parent;
    }
    
    /**
     * Builds a file tree starting in a directory.
     *
     * @param dir The root directory.
     * @param filter The file filter.
     */
    public static FileTree build(File dir,FileFilter filter) {
        FileNode node = buildNode(dir,filter);
        FileTree tree = new FileTree(node);
        node.setTree(tree);
        return tree;
    }
}
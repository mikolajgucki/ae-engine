package com.andcreations.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a file node.
 *
 * @author Mikolaj Gucki
 */
public class FileNode {
    /** The tree to which the node belogns. */
    private FileTree tree;
    
    /** The files represented by this node. */
    private List<File> files = new ArrayList<>();
    
    /** The node name. */
    private String name;
    
    /** The parent node. */
    private FileNode parent;
    
    /** The children nodes. */
    private List<FileNode> children = new ArrayList<>();
    
    /**
     * Constructs a {@link FileNode}.
     *
     * @param file The file represented by this node.
     */
    public FileNode(File file) {
        this.files.add(FileUtil.canonical(file));
    }
    
    /**
     * Constructs a {@link FileNode} with no file.
     */
    FileNode() {
    }
    
    /** */
    void setTree(FileTree tree) {
        if (tree == null) {
            return;
        }            
        
        this.tree = tree;
        for (File file:files) {
            tree.addFileToNodeEntry(file,this);
        }
        
        for (FileNode childNode:children) {
            childNode.setTree(tree);
        }
    }
    
    /**
     * Gets the file represented by this node.
     *
     * @return The file represented by this node or null if this node is not
     *   represented by a single file.
     */
    public File getFile() {
        if (files.size() != 1) {
            return null;
        }
        
        return files.get(0);
    }
    
    /** */
    private static boolean containsFile(List<File> files,File fileToCheck) {
        fileToCheck = FileUtil.canonical(fileToCheck);
        for (File file:files) {
            if (FileUtil.canonical(file).equals(fileToCheck) == true) {
                return true;
            }            
        }
        
        return false;
    }
    
    /**
     * Checks if the node contains a file.
     * 
     * @param fileToCheck The file.
     * @return <code>true</code> if contains, <code>false</code> otherwise.
     */
    public boolean containsFile(File fileToCheck) {
        return containsFile(files,fileToCheck);
    }
    
    /**
     * Adds a file to the node.
     *
     * @param file The file.
     */
    public void addFile(File file) {
        files.add(file);
        
        if (tree != null) {
            tree.addFileToNodeEntry(file,this);
        }
    }
    
    /**
     * Removes a file from the node.
     *
     * @param file The file.
     */
    public void removeFile(File file) {
        File canonicalFile = FileUtil.canonical(file);
        files.remove(canonicalFile);
        
        if (tree != null) {
            tree.removeFileToNodeEntry(canonicalFile);
        }
    }
    
    /**
     * Checks if the node represents a directory.
     *
     * @return <code>true</code> if represents, <code>false</code> otherwise.
     */
    public boolean isDirectory() {
        for (File file:files) {
            if (file.exists() == true && file.isDirectory() == false) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Checks if the node represents a file.
     *
     * @return <code>true</code> if represents, <code>false</code> otherwise.
     */
    public boolean isFile() {
        for (File file:files) {
            if (file.exists() == true && file.isFile() == false) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets the files represented by this node.
     *
     * @return The files represented by this node.
     */
    public List<File> getFiles() {
        return Collections.unmodifiableList(files);
    }
    
    /**
     * Gets the number of files represented by this node.
     *
     * @return The file count.
     */
    public int getFileCount() {
        return files.size();
    }
    
    /**
     * Gets the file name.
     *
     * @return The file name.
     */
    public String getName() {
        if (name != null) {
            return name;
        }
        if (files.size() == 0) {
            return null;
        }
        
        return files.get(0).getName();
    }
    
    /**
     * Sets the node name. If not set, the name is taken from the file if
     * only one contained in the node.
     *
     * @return The node name.
     */
    public void setName(String name) {
        this.name = name;
    }        
    
    /** */
    public void setParent(FileNode parent) {
        this.parent = parent;
    }
    
    /** */
    public FileNode getParent() {
        return parent;
    }
    
    /** */
    public String getPath(char separator) {
        String path = "";
        
        FileNode node = this;
        while (node != null) {
            if (node.getName() != null) {
                if (path.length() > 0) {
                    path = separator + path;
                }
                path = node.getName() + path;
            }
            node = node.getParent();
        }
        
        return path;
    }
    
    /** */
    public String getPath() {
        return getPath('/');
    }
    
    /** */
    public List<String> getPathToRoot() {
        List<String> path = new ArrayList<>();
        
        FileNode node = this;
        while (node != null) {
            path.add(node.getName());
            node = node.getParent();
        }
        
        Collections.reverse(path);
        return path;
    }    
    
    /**
     * Checks if the node points to at least one existing file.
     *
     * @return <code>true</code> if points to a file, <code>false</code>
     *   otherwise.
     */
    public boolean hasFiles() {
        for (File file:files) {
            if (file.exists() == true && file.isDirectory() == false) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if the node points to at least one existing directory.
     *
     * @return <code>true</code> if points to a directory, <code>false</code>
     *   otherwise.
     */
    public boolean hasDirs() {
        for (File file:files) {
            if (file.exists() == true && file.isDirectory() == true) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    public void addChildNode(FileNode node) {
        node.setParent(this);
        node.setTree(tree);
        children.add(node);
    }
    
    /** */
    public void addChildNode(FileNode node,Comparator<FileNode> comparator) {
        int index = Collections.binarySearch(children,node,comparator);
        if (index < 0) {
            index = -index - 1;
        }
        node.setParent(this);
        node.setTree(tree);
        children.add(index,node);
    }
    
    /** */
    public FileNode getChildNodeByName(String name) {
        for (FileNode node:children) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        
        return null;
    }
    
    /** */
    public FileNode getChildNodeByFile(File file) {
        for (FileNode node:children) {
            if (node.containsFile(file) == true) {
                return node;
            }
        }
        
        return null;
    }
    
    /** */
    public int indexOf(FileNode childNode) {
        return children.indexOf(childNode);
    }
    
    /** */
    public List<FileNode> getChildNodes() {
        return Collections.unmodifiableList(children);
    }
    
    /** */
    public void removeChildNode(FileNode childNode) {
        for (File file:childNode.getFiles()) {
            tree.removeFileToNodeEntry(file);
        }
        children.remove(childNode);
    }
    
    /** */
    public boolean hasChildNode(File file) {
        return getChildNodeByFile(file) != null;
    }
    
    /** */
    public void merge(FileNode thatNode) {
        if (files.isEmpty() == false && getName().equals(thatNode.getName())) {
            files.addAll(thatNode.getFiles());
        }
        
    // for each child
        for (FileNode thatChild:thatNode.getChildNodes()) {
            FileNode child = getChildNodeByName(thatChild.getName());
            if (child == null) {
                addChildNode(thatChild);
            }
            else {
                child.merge(thatChild);
            }
        }
    }
    
    /** */
    private void flatten(List<FileNode> nodes) {
        for (FileNode childNode:children) {
            nodes.add(childNode);
            childNode.flatten(nodes);
        }
    }
    
    /**
     * Gets all the children and grand children nodes as list.
     *
     * @return The list of all the nodes.
     */
    public List<FileNode> flatten() {
        List<FileNode> nodes = new ArrayList<>();
        flatten(nodes);
        
        return nodes;
    }
    
    /**
     * Sorts all the child nodes.
     *
     * @param comparator The comparator.
     */
    public void sort(Comparator<FileNode> comparator) {
        Collections.sort(children,comparator);
    }
       
    /**
     * Sorts all the child nodes and grand child nodes and so on. 
     *
     * @param comparator The comparator.
     */
    public void sortTree(Comparator<FileNode> comparator) {
        sort(comparator);
        for (FileNode childNode:children) {
            childNode.sortTree(comparator);
        }
    }
    
    /**
     * Synchronizes the node with the file system.
     *
     * @return The comparision operations.
     */
    public void syncTree(List<FileNodeSyncDiff> diffs) {
        if (files.size() != 1) {
            throw new IllegalStateException("Cannot sync multi-file node");
        }
        
        File nodeFile = getFile();
        if (nodeFile.exists() == false) {
            diffs.add(FileNodeSyncDiff.deleted(nodeFile));
            return;
        }
        if (nodeFile.isDirectory() == false) {
            return;
        }
        
        List<File> fsFiles = Arrays.asList(nodeFile.listFiles());
    // added
        for (File fsFile:fsFiles) {
            if (hasChildNode(fsFile) == false) {
                diffs.add(FileNodeSyncDiff.added(fsFile));
            }
        }    
        
    // children
        for (FileNode childNode:getChildNodes()) {
            childNode.syncTree(diffs);
        }
    }
    
    /** */
    private void append(StringBuilder builder,int indent,String str) {
        while (indent > 0) {
            builder.append(' ');
            indent--;
        }
        builder.append(str + "\n");        
    }
    
    /** */
    String toString(int indent,boolean ignoreThisNode) {
        final int indentSize = 4;
        
        StringBuilder builder = new StringBuilder();
        if (ignoreThisNode == false) {
            if (files.size() == 1) {
                File file = getFile();
                append(builder,indent,String.format("%s [%s]",getName(),
                    file.getAbsolutePath()));
                indent += indentSize;
            }
            if (files.size() > 1) {
                append(builder,indent,getName());
                indent += indentSize;
                for (File file:files) {
                    append(builder,indent,String.format(" [%s]",
                        file.getAbsolutePath()));
                }
            }
        }
        
        for (FileNode node:children) {
            builder.append(node.toString(indent,false));
        }
        
        return builder.toString();
    }
    
    /** */
    @Override    
    public String toString() {
        return toString(0,false);
    }
}
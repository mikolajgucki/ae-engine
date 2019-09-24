package com.andcreations.ae.studio.plugins.lua.explorer.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaSourceTreeNode extends DefaultMutableTreeNode {
    /** */
    private FileNode fileNode;
    
    /** */
    LuaSourceTreeNode() {
    }
    
    /** */
    LuaSourceTreeNode(FileNode fileNode) {
        this.fileNode = fileNode;        
        setUserObject(fileNode.getName());
    }
    
    /** */
    public FileNode getFileNode() {
        return fileNode;
    }
}
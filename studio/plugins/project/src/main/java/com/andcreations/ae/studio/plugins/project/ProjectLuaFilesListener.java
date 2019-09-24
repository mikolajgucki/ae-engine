package com.andcreations.ae.studio.plugins.project;

import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public interface ProjectLuaFilesListener {
    /**
     * Called when a Lua source file node has changed.
     *
     * @param node The node.
     */
    void luaSourceFileNodeChanged(FileNode node);
    
    /** 
     * Called when a node has been added to another node.
     *
     * @param parentNode The parent node.
     * @param node The node.
     */
    void luaSourceFileNodeAdded(FileNode parentNode,FileNode node);
    
    
    /** 
     * Called when a node has been removed.
     *
     * @param parentNode The parent node.
     * @param node The node.
     * @param index The index of the node in the parent node before removing.
     */
    void luaSourceFileNodeRemoved(FileNode parentNode,FileNode node,int index);    
}

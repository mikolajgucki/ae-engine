package com.andcreations.ae.studio.plugins.lua.explorer.tree;

import java.io.File;

import javax.swing.tree.DefaultTreeModel;

import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaSourceTreeModel extends DefaultTreeModel {
    /** */
    private LuaSourceTreeNode root;
    
    /** */
    private FileTree fileTree;
    
    /** */
    LuaSourceTreeModel(LuaSourceTreeNode root) {
        super(root);
        this.root = root;
        create();        
    }
    
    /** */
    private void create() {
        fileTree = ProjectLuaFiles.get().getLuaSourceTree();
        fileTree.sort();
        //addNodes(root,fileTree.getRoot());
        buildTree(root);
    }

    /** */
    void buildTree(LuaSourceTreeNode node) {
        buildTree(node,node.getFileNode());
    }
    
    /** */
    private void buildTree(LuaSourceTreeNode node,FileNode fileNode) {
        for (FileNode childFileNode:fileNode.getChildNodes()) {
            LuaSourceTreeNode childNode = new LuaSourceTreeNode(childFileNode);
            node.add(childNode);
            
        // don't go deeper if the child node has errors
            if (ProjectLuaFiles.getError(childFileNode) == null) {
                buildTree(childNode,childFileNode);
            }
        }
    }
    
    /** */
    public LuaSourceTreeNode findNode(LuaSourceTreeNode node,
        FileNode fileNode) {
    //
        if (node.getFileNode() == fileNode) {
            return node;
        }
        
        for (int index = 0; index < getChildCount(node); index++) {
            LuaSourceTreeNode childNode =
                (LuaSourceTreeNode)getChild(node,index);
            LuaSourceTreeNode foundNode = findNode(childNode,fileNode);
            if (foundNode != null) {
                return foundNode;
            }
        }
        
        return null;
    }
    
    
    /** */
    LuaSourceTreeNode findNode(FileNode fileNode) {
        return findNode(root,fileNode);
    }    
    
    /** */
    public LuaSourceTreeNode findNode(File file) {
        FileNode fileNode = fileTree.findNode(file);
        return findNode(root,fileNode);
    }
}
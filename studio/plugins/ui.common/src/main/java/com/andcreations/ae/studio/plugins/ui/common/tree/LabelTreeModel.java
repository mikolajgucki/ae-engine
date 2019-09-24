package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;

/**
 * @author Mikolaj Gucki    
 */
@SuppressWarnings("serial")
public class LabelTreeModel extends DefaultTreeModel {
    /** */
    public LabelTreeModel(LabelTreeNode root) {
        super(root);
    }
    
    /** */
    private void flatten(LabelTreeNode node,List<LabelTreeNode> nodes) {
        nodes.add(node);
        
        Enumeration<?> children = node.children();
        while (children.hasMoreElements()) {
            LabelTreeNode childNode = (LabelTreeNode)children.nextElement();
            flatten(childNode,nodes);
        }
    }
    
    /** */
    public List<LabelTreeNode> flatten() {
        List<LabelTreeNode> nodes = new ArrayList<>();
        flatten((LabelTreeNode)getRoot(),nodes);
        
        return nodes;
    }  
    
    /** */
    public LabelTreeNode findNodeByUserObject(Object userObject) {
        List<LabelTreeNode> nodes = flatten();        
        for (LabelTreeNode node:nodes) {
            if (node.getUserObject() == userObject) {
                return node;
            }
        }
        
        return null;
    }   
}
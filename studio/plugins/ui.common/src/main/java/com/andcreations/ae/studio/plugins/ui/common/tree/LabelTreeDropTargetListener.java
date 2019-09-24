package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.tree.TreePath;

/**
 * @author Mikolaj Gucki    
 */
class LabelTreeDropTargetListener extends DropTargetAdapter {
    /** */
    private LabelTree tree;
    
    /** */
    LabelTreeDropTargetListener(LabelTree tree) {
        this.tree = tree;
    }
    
    /** */
    @Override
    public void dragOver(DropTargetDragEvent event) {
        TreePath path = tree.getPathForLocation(
            event.getLocation().x,event.getLocation().y);
        if (path == null) {
            event.rejectDrag();
            return;
        }        
        
        Object component = path.getLastPathComponent();
    // pass to tree node
        LabelTreeNode labelTreeNode = (LabelTreeNode)component;
        if (labelTreeNode.dragOver(event) == true) {        
            tree.setSelectionPath(path);
        }
    }    
    
    /** */
    @Override
    public void drop(DropTargetDropEvent event) {
        TreePath path = tree.getPathForLocation(
            event.getLocation().x,event.getLocation().y);
        if (path == null) {
            event.rejectDrop();
            return;
        }
        
        Object component = path.getLastPathComponent();
    // pass to tree node
        LabelTreeNode labelTreeNode = (LabelTreeNode)component;
        labelTreeNode.drop(event);
    }
}
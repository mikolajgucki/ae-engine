package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

/**
 * @author Mikolaj Gucki    
 */
public abstract class LabelTreeNodeAdapter implements LabelTreeNodeListener {
    /** */
    @Override
    public void labelTreeNodeSelected(LabelTreeNode node) {
    }
    
    /** */
    @Override
    public void labelTreeNodeDoubleClicked(LabelTreeNode node) {
    }
    
    /** */
    @Override
    public void labelTreeNodeExpanded(LabelTreeNode node) {
    }
    
    /** */
    @Override
    public void labelTreeNodeCollapsed(LabelTreeNode node) {
    }
    
    /** */
    @Override
    public boolean dragOver(LabelTreeNode node,DropTargetDragEvent event) {
        return false;
    }
    
    /** */
    @Override
    public boolean drop(LabelTreeNode node,DropTargetDropEvent event) {
        return false;
    }
}
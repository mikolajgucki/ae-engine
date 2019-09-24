package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

/**
 * @author Mikolaj Gucki    
 */
public interface LabelTreeNodeListener {
    /**
     * Called when a node has been selected.
     *
     * @param node The selected node.
     */
    void labelTreeNodeSelected(LabelTreeNode node);
    
    /**
     * Called when a node has been double-clicked.
     *
     * @param node The selected node.
     */
    void labelTreeNodeDoubleClicked(LabelTreeNode node);    
    
    /**
     * Called when a node has been expanded.
     *
     * @param node The expanded node.
     */
    void labelTreeNodeExpanded(LabelTreeNode node);
    
    /**
     * Called when a node has been collapsed.
     *
     * @param node The collapsed node.
     */
    void labelTreeNodeCollapsed(LabelTreeNode node);
    
    /**
     * Called on drag over event.
     *
     * @param node The node over which the drag was triggered.
     * @param event The event.
     * @return <code>true</code> if the event is consumed, <code>false</code>
     *   otherwise.
     */
    boolean dragOver(LabelTreeNode node,DropTargetDragEvent event);
    
    /**
     * Called on drop event.
     *
     * @param node The node over which to drop the data.
     * @param event The event.   
     * @return <code>true</code> if the event is consumed, <code>false</code>
     *   otherwise.     
     */
    boolean drop(LabelTreeNode node,DropTargetDropEvent event);
}
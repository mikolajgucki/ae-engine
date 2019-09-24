package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

/**
 * @author Mikolaj Gucki
 */
class LabelTreeDragGestureListener implements DragGestureListener {
    /** */
    private LabelTree tree;
    
    /** */
    LabelTreeDragGestureListener(LabelTree tree) {
        this.tree = tree;
    }
    
    /** */
    @Override    
    public void dragGestureRecognized(DragGestureEvent event) {
        tree.dragGestureRecognized(event);       
    }
}
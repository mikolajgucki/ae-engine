package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DragGestureEvent;

/**
 * @author Mikolaj Gucki
 */
public interface LabelTreeListener {
    /**
     * Called when a drag gesture has been recognized.
     *
     * @param event The event.
     * @return <code>true</code> if the event has been consumed,
     *   <code>false</code> otherwise.
     */
    boolean dragGestureRecognized(DragGestureEvent event);
}
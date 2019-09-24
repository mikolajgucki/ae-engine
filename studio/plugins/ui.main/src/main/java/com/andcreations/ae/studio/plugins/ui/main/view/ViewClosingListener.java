package com.andcreations.ae.studio.plugins.ui.main.view;

/**
 * The interface for the view closing listeners.
 *
 * @author Mikolaj Gucki
 */
public interface ViewClosingListener {
    /**
     * Called when a view is about to be closed.
     *
     * @param view The view.
     * @return <code>true</code> if to close the view, <code>false</code> if
     *   to keep the view.
     */
    boolean viewClosing(View view);
}

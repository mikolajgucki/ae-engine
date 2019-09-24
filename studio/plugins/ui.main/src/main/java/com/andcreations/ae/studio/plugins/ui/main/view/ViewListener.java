package com.andcreations.ae.studio.plugins.ui.main.view;

/**
 * The interface for the view listeners.
 *
 * @author Mikolaj Gucki
 */
public interface ViewListener {
    /**
     * Called when a view lost focus.
     *
     * @param view The view.
     */
    void viewFocusLost(View view);
    
    /**
     * Called when a view gained focus.
     *
     * @param view The view.
     */
    void viewFocusGained(View view);
    
    /**
     * Called when a view has been shown.
     *
     * @param view The view.
     */
    void viewShown(View view);
    
    /**
     * Called when a view has been closed.
     *
     * @param view The view.
     */
    void viewClosed(View view);
}

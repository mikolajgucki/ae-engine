package com.andcreations.ae.studio.plugins.ui.main.view;

/**
 * An abstract view listener which does nothing.
 *
 * @author Mikolaj Gucki
 */
public abstract class ViewAdapter implements ViewListener {
    /** */
    @Override
    public void viewFocusLost(View view) {
    }
    
    /** */
    @Override
    public void viewFocusGained(View view) {
    }
    
    /** */
    @Override
    public void viewShown(View view) {
    }
    
    /** */
    @Override
    public void viewClosed(View view) {
    }
}

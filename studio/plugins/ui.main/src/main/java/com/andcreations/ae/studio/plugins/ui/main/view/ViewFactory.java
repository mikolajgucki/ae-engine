package com.andcreations.ae.studio.plugins.ui.main.view;

/**
 * Creates views.
 * 
 * @author Mikolaj Gucki
 */
public interface ViewFactory {
    /**
     * Gets the view factory identifier.
     * 
     * @return The identifier.
     */
    String getId();
    
    /**
     * Creates a view.
     * 
     * @param viewId The view identifier unique within the factory.
     * @return The view.
     */
    View createView(String viewId);
}

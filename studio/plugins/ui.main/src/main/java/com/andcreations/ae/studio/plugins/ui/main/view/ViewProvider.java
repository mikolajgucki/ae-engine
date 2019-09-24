package com.andcreations.ae.studio.plugins.ui.main.view;

import javax.swing.ImageIcon;

/**
 * Provides a view.
 * 
 * @author Mikolaj Gucki
 */
public interface ViewProvider {
    /**
     * Gets the view title.
     *
     * @return The view title.
     */
    String getTitle();
    
    /**
     * Gets the view icon.
     *
     * @return The view icon.
     */
    ImageIcon getIcon();
    
    /**
     * Shows the view.
     */
    void showView();
}

package com.andcreations.ae.studio.plugins.ui.main.view;

import java.awt.Component;

import javax.swing.ImageIcon;

/**
 * Represents a view.
 * 
 * @author Mikolaj Gucki
 */
public interface View {
    /**
     * Gets the view manager.
     * 
     * @return The view manager.
     */
    ViewManager getManager();
    
    /**
     * Gets the factory which created the view.
     * 
     * @return The view factory.
     */
    ViewFactory getFactory();
    
    /**
     * Gets the view identifier passed to the view factory.
     *
     * @return The view identifier passed to the view factory.
     */
    String getViewId();
    
    /**
     * Sets the view title.
     * 
     * @param title The view title.
     */
    void setTitle(String title);
    
    /**
     * Gets the view title.
     *
     * @return The view title.
     */
    String getTitle();
    
    /**
     * Sets the view icon.
     * 
     * @param icon The view icon.
     */
    void setIcon(ImageIcon icon);
    
    /**
     * Gets the view icon.
     *
     * @return The view icon.
     */
    ImageIcon getIcon();
    
    /**
     * Sets the category to which the view belongs.
     *
     * @param category The category.
     */
    void setCategory(ViewCategory category);
    
    /**
     * Gets the category to which the view belongs.
     *
     * @return The category.
     */
    ViewCategory getCategory();
    
    /**
     * Sets the view component.
     * 
     * @param component The view component.
     */
    void setComponent(Component component);
    
    /**
     * Gets the view component.
     *
     * @return The view component.
     */
    Component getComponent();
    
    /**
     * Adds a view listener.
     *
     * @param listener The listener.
     */
    void addViewListener(ViewListener listener);
    
    /**
     * Sets the view closing listener.
     *
     * @param listener The listener.
     */
    void setViewClosingListener(ViewClosingListener listener);
    
    /**
     * Adds a button.
     *
     * @return The new button.
     */
    ViewButton addButton();
    
    /**
     * Adds a drop down.
     *
     * @return The new drop down.
     */
    ViewDropDown addDropDown();
    
    /**
     * Adds a check button.
     *
     * @return The new button.
     */
    ViewCheckButton addCheckButton();
}

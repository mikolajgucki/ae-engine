package com.andcreations.ae.studio.plugins.ui.main.view;

import java.awt.Component;
import java.util.List;
import java.util.Set;

/**
 * Manages views.
 * 
 * @author Mikolaj Gucki
 */
public interface ViewManager {
    /**
     * Gets the component which displays the views managed by this manager.
     *
     * @return The component.
     */
    Component getComponent();
    
    /**
     * Adds a view manager listener.
     *
     * @param listener The listener.
     */
    void addViewManagerListener(ViewManagerListener listener);
    
    /**
     * Registers a view factory.
     * 
     * @param viewFactory The view factory.
     */
    void registerViewFactory(ViewFactory viewFactory);
    
    /**
     * Creates a view. 
     *  
     * @param viewFactory The factory which is to create the view.
     * @param viewId The view identifier.
     * @return The view.
     */
    View createView(ViewFactory viewFactory,String viewId);
    
    /**
     * Shows a view.
     *
     * @param view The view.
     */
    void showView(View view);
    
    /**
     * Closes a view.
     *
     * @param view The view.
     */
    void closeView(View view);
    
    /**
     * Gets the view manager state.
     * 
     * @return The view manager state.
     */
    String getState();
    
    /**
     * Sets the view manager state.
     * 
     * @param state The view manager state.
     */
    void setState(String state);
    
    /**
     * Restores the views from the state.
     */
    void restoreViews();
    
    /**
     * Restores the views from the state.
     */
    void restoreViewLayout();
    
    /**
     * Adds a view provider.
     * 
     * @param viewProvider The view provider.
     */
    void addViewProvider(ViewProvider viewProvider);
    
    /**
     * Gets the view providers.
     * 
     * @return The view providers.
     */
    List<ViewProvider> getViewProviders();
    
    /**
     * Gets a view by title.
     * 
     * @param title The title.
     * @return The view of the title or null if there is no such view.
     */
    View getViewByTitle(String title);
    
    /**
     * Gets a view by factory and identifier.
     *
     * @param viewFactory The view factory.
     * @param viewId The view identifier.     
     * @return The view of the identifier or null if there is no such view.
     */
    View getView(ViewFactory viewFactory,String viewId);
    
    /**
     * Gets all the open views.
     *
     * @return The views.
     */
    Set<View> getViews();
    
    /**
     * Traverses the focus to a given view.
     *
     * @param view The view to focus on.
     */
    void focus(View view);
    
    /**
     * Goes to the next view in the group with the focused view.
     */
    void goToNextView();
    
    /**
     * Goes to the previous view in the group with the focused view.
     */
    void goToPreviousView();
    
    /**
     * Gets all the sibling views of a view.
     *
     * @param view The view for which to get siblings.
     */
    List<View> getSiblingViews(View view);
}

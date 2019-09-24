package com.andcreations.ae.studio.plugins.ui.main.view.impl;

/**
 * Represents the state of the view implementation.
 * 
 * @author Mikolaj Gucki
 */
class ViewImplState {
    /** The identifier of the factory which created the view. */
    private String factoryId;
    
    /** The view identifier.*/
    private String viewId;
    
    /** The view title. */
    private String title;
    
    /** */
    ViewImplState() {
    }
    
    /** */
    ViewImplState(String factoryId,String viewId,String title) {
        this.factoryId = factoryId;
        this.viewId = viewId;
        this.title = title;
    }
    
    /** */
    void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }
    
    /** */
    String getFactoryId() {
        return factoryId;
    }
    
    /** */
    void setViewId(String viewId) {
        this.viewId = viewId;
    }
    
    /** */
    String getViewId() {
        return viewId;
    }
    
    /** */
    void setTitle(String title) {
        this.title = title;
    }     
    
    /** */
    String getTitle() {
        return title;
    }
}

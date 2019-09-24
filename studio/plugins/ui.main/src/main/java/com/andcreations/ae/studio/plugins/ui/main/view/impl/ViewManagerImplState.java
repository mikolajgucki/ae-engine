package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.ui.main.view.impl.resources.R;
import com.andcreations.io.json.JSON;
import com.andcreations.io.json.JSONException;
import com.andcreations.resources.ResourceLoader;

/**
 * The state of the view manager implementation.
 * 
 * @author Mikolaj Gucki
 */
class ViewManagerImplState {
    /** The states of the view states. */
    private List<ViewImplState> viewImplStateList = new ArrayList<>();
    
    /** The dockable layout. */
    private String dockFrontendLayout;
    
    /** The title of the view with focus. */
    private String focusedView;
    
    /** */
    void loadDefault() {
        String src;
        try {
            src = ResourceLoader.loadAsString(
                R.class,"ViewManagerImplState.state");
        } catch (IOException exception) {
            Log.error("Failed to read default layout: " + exception.getMessage());
            return;
        }        
        
        ViewManagerImplState defaultState;
        try {
            defaultState = JSON.read(src,ViewManagerImplState.class);
        } catch (JSONException exception) {
            Log.error("Failed to read default layout: " +
                exception.getMessage());
            return;
        }
        
        viewImplStateList = defaultState.getViewImplStateList();
        dockFrontendLayout = defaultState.getDockFrontendLayout();
        focusedView = defaultState.getFocusedView();
    }
    
    /** */
    private ViewImplState getViewImplState(String factoryId,String viewId) {
        for (ViewImplState viewImplState:viewImplStateList) {
            if (StringUtils.equals(viewImplState.getFactoryId(),factoryId) &&
                StringUtils.equals(viewImplState.getViewId(),viewId)) {
            //
                return viewImplState;
            }
        }
        
        return null;
    }
    
    /** */
    void addViewImplState(String factoryId,String viewId,String title) {
        if (getViewImplState(factoryId,viewId) != null) {
            return;
        }        
        viewImplStateList.add(new ViewImplState(factoryId,viewId,title));
    }
    
    /** */
    void removeViewImplState(String factoryId,String viewId) {
        ViewImplState toRemove = getViewImplState(factoryId,viewId);
        if (toRemove != null) {
            viewImplStateList.remove(toRemove);
        }
    }
    
    /** */
    void setViewImplStateList(List<ViewImplState> viewImplStateList) {
        this.viewImplStateList = viewImplStateList;
    }
    
    /** */
    List<ViewImplState> getViewImplStateList() {
        return viewImplStateList;
    }
    
    /** */
    void setDockFrontendLayout(String layout) {
        this.dockFrontendLayout = layout;
    }
    
    /** */
    String getDockFrontendLayout() {
        return dockFrontendLayout;
    }
    
    /** */
    void setFocusedView(String focusedView) {
        this.focusedView = focusedView;
    }
    
    /** */
    String getFocusedView() {
        return focusedView;
    }
}

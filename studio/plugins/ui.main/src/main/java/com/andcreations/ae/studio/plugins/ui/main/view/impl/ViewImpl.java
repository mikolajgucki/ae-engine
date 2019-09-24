package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import bibliothek.gui.dock.DefaultDockable;

import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewClosingListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDropDown;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;

/**
 * The view implementation based on the Docking Frames library. 
 * 
 * @author Mikolaj Gucki
 */
public class ViewImpl implements View {
    /** The parent manager. */
    private ViewManager manager;
    
    /** The view factory which created the view. */
    private ViewFactory factory;

    /** The view identifier passed to the view factory. */
    private String viewId;
    
    /** The view title. */
    private String title;
    
    /** The view icon. */
    private ImageIcon icon;
    
    /** The view category. */
    private ViewCategory category;
    
    /** The component. */
    private Component component;
    
    /** The dockable associated with this view. */
    private DefaultDockable dockable;
    
    /** The view listeners. */
    private List<ViewListener> listeners = new ArrayList<>();
    
    /** The view closing listener. */
    private ViewClosingListener closingListener;
    
    /** The view actions. */
    private List<ViewAction> actions = new ArrayList<>();
    
    /**
     * Constructs a {@link ViewImpl}.
     * 
     * @param manager The parent manager.
     * @param factory The factory which created the view.
     * @param viewId The view identifier passed to the view factory.
     */
    ViewImpl(ViewManager manager,ViewFactory factory,String viewId) {
        this.manager = manager;
        this.factory = factory;
        this.viewId = viewId;
    }
    
    /** */
    @Override
    public ViewManager getManager() {
        return manager;
    }
    
    /** */
    @Override
    public ViewFactory getFactory() {
        return factory;
    }
    
    /** */
    @Override
    public String getViewId() {
        return viewId;
    }
    
    /** */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    /** */
    @Override
    public String getTitle() {
        return title;
    }
    
    /** */
    @Override
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        if (dockable != null) {
            dockable.setTitleIcon(icon);
        }
    }
    
    /** */
    @Override
    public ImageIcon getIcon() {
        return icon;
    }
    
    /** */
    @Override
    public void setCategory(ViewCategory category) {
        this.category = category;
    }
    
    /** */
    public ViewCategory getCategory() {
        return category;
    }
    
    /** */
    @Override
    public void setComponent(Component component) {
        this.component = component;
    }
    
    /** */
    @Override
    public Component getComponent() {
        return component;
    }
    
    /** */
    @Override
    public ViewButton addButton() {
        ViewButtonImpl button = new ViewButtonImpl();
        actions.add(button);
        return button;
    }
    
    /** */
    @Override
    public ViewDropDown addDropDown() {
        ViewDropDownImpl dropDown = new ViewDropDownImpl();
        actions.add(dropDown);
        return dropDown;
    }
    
    /** */
    @Override
    public ViewCheckButton addCheckButton() {
        ViewCheckButtonImpl button = new ViewCheckButtonImpl();
        actions.add(button);
        return button;        
    }
    
    /** */
    List<ViewAction> getActions() {
        return actions;
    }
    
    /** */
    void setDockable(DefaultDockable dockable) {
        this.dockable = dockable;
    }
    
    /** */
    DefaultDockable getDockable() {
        return dockable;
    }
    
    /** */
    @Override
    public void addViewListener(ViewListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    @Override
    public void setViewClosingListener(ViewClosingListener listener) {
        this.closingListener = listener;
    }
    
    /** */
    void notifyFocusLost() {
        synchronized (listeners) {
            for (ViewListener listener:listeners) {
                listener.viewFocusLost(this);
            }
        }
    }
    
    /** */
    void notifyFocusGained() {
        synchronized (listeners) {
            for (ViewListener listener:listeners) {
                listener.viewFocusGained(this);
            }
        }
    }
    
    /** */
    void notifyShown() {
        synchronized (listeners) {
            for (ViewListener listener:listeners) {
                listener.viewShown(this);
            }
        }
    }
    
    /** */
    void notifyClosed() {
        synchronized (listeners) {
            for (ViewListener listener:listeners) {
                listener.viewClosed(this);
            }
        }
    }
    
    /** */
    boolean closing() {
        if (closingListener == null) {
            return true;
        }
        
        return closingListener.viewClosing(this);
    }
}

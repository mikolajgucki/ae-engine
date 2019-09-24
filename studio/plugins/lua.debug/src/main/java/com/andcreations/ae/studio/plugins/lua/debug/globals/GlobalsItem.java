package com.andcreations.ae.studio.plugins.lua.debug.globals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class GlobalsItem extends LabelTreeNode {
    /** */
    private Object object;
    
    /** */
    private List<GlobalsItemListener> listeners = new ArrayList<>();
    
    /**
     * Constructs a {@link GlobalsItem}.
     *
     * @param icon The icon.
     * @param value The value.
     */
    public GlobalsItem(ImageIcon icon,String value) {
        super(icon,value);
    }
    
    /**
     * Constructs a {@link GlobalsItem}.
     *
     * @param icon The icon.
     * @param value The value.
     * @param htmlValue The value in HTML.
     */
    public GlobalsItem(ImageIcon icon,String value,String htmlValue) {
        super(icon,value,htmlValue);
    }
    
    /**
     * Constructs a {@link GlobalsItem}.
     *
     * @param icon The icon.
     * @param value The value.
     * @param htmlValue The value in HTML.
     * @param object The user object.
     */
    public GlobalsItem(ImageIcon icon,String value,String htmlValue,
        Object object) {
    //
        super(icon,value,htmlValue);
        this.object = object;
    }
    
    /**
     * Gets the user object.
     *
     * @return The user object.
     */
    public Object getObject() {
        return object;
    }
    
    /** */
    public void addGlobalsItemListener(GlobalsItemListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void removeGlobalsItemListener(GlobalsItemListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    @Override
    protected void notifySelected() {
        synchronized (listeners) {
            for (GlobalsItemListener listener:listeners) {
                listener.globalsItemSelected(this);
            }
        }
    }  
    
    /** */
    @Override
    protected void notifyDoubleClicked() {
        synchronized (listeners) {
            for (GlobalsItemListener listener:listeners) {
                listener.globalsItemDoubleClicked(this);
            }
        }
    }
    
    /** */
    @Override
    protected void notifyExpanded() {
        synchronized (listeners) {
            for (GlobalsItemListener listener:listeners) {
                listener.globalsItemExpanded(this);
            }
        }
    }    
}
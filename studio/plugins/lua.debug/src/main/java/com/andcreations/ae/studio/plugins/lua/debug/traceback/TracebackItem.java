package com.andcreations.ae.studio.plugins.lua.debug.traceback;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class TracebackItem extends LabelTreeNode {
    /** */
    private Object object;
    
    /** */
    private List<TracebackItemListener> listeners = new ArrayList<>();
    
    /**
     * Creates a {@link TracebackItem}.
     *
     * @param icon The icon.
     * @param value The value.
     */
    public TracebackItem(ImageIcon icon,String value) {
        super(icon,value);
    }    
    
    /**
     * Creates a {@link TracebackItem}.
     *
     * @param icon The icon.
     * @param value The value.
     * @param htmlValue The value in HTML.
     */
    public TracebackItem(ImageIcon icon,String value,String htmlValue) {
        super(icon,value,htmlValue);
    }
    
    /**
     * Creates a {@link TracebackItem}.
     *
     * @param icon The icon.
     * @param value The display value.
     * @param htmlValue The value in HTML.
     * @param object The user object.
     */
    public TracebackItem(ImageIcon icon,String value,String htmlValue,
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
    public void addTracebackItemListener(TracebackItemListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void removeTracebackItemListener(TracebackItemListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    @Override
    protected void notifySelected() {
        synchronized (listeners) {
            for (TracebackItemListener listener:listeners) {
                listener.tracebackItemSelected(this);
            }
        }
    }  
    
    /** */
    @Override
    protected void notifyDoubleClicked() {
        synchronized (listeners) {
            for (TracebackItemListener listener:listeners) {
                listener.tracebackItemDoubleClicked(this);
            }
        }
    }
    
    /** */
    @Override
    protected void notifyExpanded() {
        synchronized (listeners) {
            for (TracebackItemListener listener:listeners) {
                listener.tracebackItemExpanded(this);
            }
        }
    }    
}
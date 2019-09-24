package com.andcreations.ae.studio.plugins.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public class SearchItem {
    /** */
    private ImageIcon icon;
    
    /** */
    private String value;
    
    /** */
    private String htmlValue;
    
    /** */
    private Object object;
    
    /** */
    private List<SearchItem> childItems = new ArrayList<>();
    
    /** */
    private List<SearchItemListener> listeners = new ArrayList<>();
    
    /**
     * Creates a {@link SearchItem}.
     *
     * @param icon The icon.
     * @param value The value.
     */
    public SearchItem(ImageIcon icon,String value) {
        this(icon,value,null,null);
    }    
    
    /**
     * Creates a {@link SearchItem}.
     *
     * @param icon The icon.
     * @param value The value.
     * @param htmlValue The value in HTML.
     */
    public SearchItem(ImageIcon icon,String value,String htmlValue) {
        this(icon,value,htmlValue,null);
    }
    
    /**
     * Creates a {@link SearchItem}.
     *
     * @param icon The icon.
     * @param value The display value.
     * @param htmlValue The value in HTML.
     * @param object The user object.
     */
    public SearchItem(ImageIcon icon,String value,String htmlValue,
        Object object) {
    //
        this.icon = icon;
        this.value = value;
        this.htmlValue = htmlValue;
        this.object = object;
    }       
    
    /**
     * Gets the icon.
     *
     * @return The icon.
     */
    public ImageIcon getIcon() {
        return icon;
    }
    
    /**
     * Sets the value.
     *
     * @param value The value.
     */
    void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Gets the value.
     *
     * @return The value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Gets the value in HTML
     *
     * @return The value in HTML.
     */
    public String getHTMLValue() {
        return htmlValue;
    }
    
    /**
     * Gets the user object.
     *
     * @return The user object.
     */
    public Object getObject() {
        return object;
    }    
    
    /**
     * Adds a child item.
     *
     * @param item The child item.
     */
    public void addChildItem(SearchItem item) {
        childItems.add(item);
    }
    
    /**
     * Gets the child items.
     *
     * @return The child items.
     */
    public List<SearchItem> getChildItems() {
        return Collections.unmodifiableList(childItems);
    }     
    
    /**
     * Adds an item listener.
     *
     * @param listener The listener.
     */
    public void addSearchItemListener(SearchItemListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
}
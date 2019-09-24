package com.andcreations.ae.studio.plugins.outline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public class OutlineItem {
    /** */
    private ImageIcon icon;
    
    /** */
    private String value;
    
    /** */
    private String htmlValue;
    
    /** */
    private Object object;
    
    /** */
    private List<OutlineItem> childItems = new ArrayList<>();
    
    /** */
    private List<OutlineItemListener> listeners = new ArrayList<>();
    
    /**
     * Creates a {@link OutlineItem}.
     *
     * @param icon The icon.
     * @param value The value.
     */
    public OutlineItem(ImageIcon icon,String value) {
        this(icon,value,null,null);
    }    
    
    /**
     * Creates a {@link OutlineItem}.
     *
     * @param icon The icon.
     * @param value The value.
     * @param htmlValue The value in HTML.
     */
    public OutlineItem(ImageIcon icon,String value,String htmlValue) {
        this(icon,value,htmlValue,null);
    }
    
    /**
     * Creates a {@link OutlineItem}.
     *
     * @param icon The icon.
     * @param value The display value.
     * @param htmlValue The value in HTML.
     * @param object The user object.
     */
    public OutlineItem(ImageIcon icon,String value,String htmlValue,
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
     * Sets the HTML value.
     *
     * @param htmlValue The HTML value.
     */
    void setHTMLValue(String htmlValue) {
        this.htmlValue = htmlValue;
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
    public void addChildItem(OutlineItem item) {
        childItems.add(item);
    }
    
    /**
     * Adds child items.
     *
     * @param item The child item.
     */
    public void addChildItems(List<? extends OutlineItem> items) {
        childItems.addAll(items);
    }
    
    /**
     * Gets the child items.
     *
     * @return The child items.
     */
    public List<OutlineItem> getChildItems() {
        return Collections.unmodifiableList(childItems);
    }     
    
    /**
     * Adds an item listener.
     *
     * @param listener The listener.
     */
    public void addOutlineItemListener(OutlineItemListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void notifySelected() {
        synchronized (listeners) {
            for (OutlineItemListener listener:listeners) {
                listener.itemSelected(this);
            }
        }
    }
    
    /** */
    private OutlineItem findByValue(String value) {
        for (OutlineItem childItem:childItems) {
            if (value.equals(childItem.getValue()) == true) {
                return childItem;
            }
        }
        
        return null;
    }
    
    /** */
    private boolean equalValues(OutlineItem that) {
        if (Objects.equals(value,that.value) == false) {
            return false;
        }
        if (Objects.equals(htmlValue,that.htmlValue) == false) {
            return false;
        }
        if (Objects.equals(object,that.object) == false) {
            return false;
        }
        
        return true;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof OutlineItem) == false) {
            return false;
        }
        OutlineItem that = (OutlineItem)obj;
        
    // values
        if (equalValues(that) == false) {
            return false;
        }    
        
        for (OutlineItem childItem:childItems) {
            OutlineItem thatChildItem = that.findByValue(childItem.getValue());
            if (thatChildItem == null) {
                return false;
            }
            if (childItem.equals(thatChildItem) == false) {
                return false;
            }
        }
        for (OutlineItem thatChildItem:that.childItems) {
            OutlineItem childItem = findByValue(thatChildItem.getValue());
            if (childItem == null) {
                return false;
            }
            if (thatChildItem.equals(childItem) == false) {
                return false;
            }
        }
        
        return true;
    }
}
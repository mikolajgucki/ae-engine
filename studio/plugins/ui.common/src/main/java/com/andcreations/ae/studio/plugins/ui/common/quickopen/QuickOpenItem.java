package com.andcreations.ae.studio.plugins.ui.common.quickopen;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public class QuickOpenItem {
    /** */
    private ImageIcon icon;
    
    /** */
    private String searchValue;
    
    /** */
    private String value;
    
    /** */
    private String htmlValue;
    
    /** */
    private Object object; 
    
    /**
     * Creates a {@link QuickOpenItem}.
     *
     * @param icon The icon.
     * @param searchValue The search value.
     * @param value The plain value.
     * @param object The user object.
     */
    public QuickOpenItem(ImageIcon icon,String searchValue,String value,
        Object object) {
    //
        this(icon,searchValue,value,value,object);
    }    
    
    /**
     * Creates a {@link QuickOpenItem}.
     *
     * @param icon The icon.
     * @param searchValue The search value.
     * @param value The plain value.
     * @param htmlValue The HTML value.
     * @param object The user object.
     */
    public QuickOpenItem(ImageIcon icon,String searchValue,String value,
        String htmlValue,Object object) {
    //
        this.icon = icon;
        this.searchValue = searchValue;
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
     * Gets the search value.
     *
     * @return The search value.
     */
    public String getSearchValue() {
        return searchValue;
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
}
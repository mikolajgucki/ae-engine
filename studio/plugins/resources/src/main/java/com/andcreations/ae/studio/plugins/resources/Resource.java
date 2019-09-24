package com.andcreations.ae.studio.plugins.resources;

/**
 * @author Mikolaj Gucki
 */
public class Resource {
    /** */
    private String iconName;
    
    /** */
    private String name;
    
    /** */
    private String details;
    
    /** */
    private Object userObject;
    
    /** */
    public Resource(String iconName,String name) {
        this.iconName = iconName;
        this.name = name;
    }
    
    /** */
    public Resource(String iconName,String name,Object userObject) {
        this.iconName = iconName;
        this.name = name;
        this.userObject = userObject;
    }
    
    
    /** */
    public Resource(String iconName,String name,String details,
        Object userObject) {
    //
        this.iconName = iconName;
        this.name = name;
        this.details = details;
        this.userObject = userObject;
    }    
    
    /** */
    public String getIconName() {
        return iconName;
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    public String getDetails() {
        return details;
    }
    
    /** */
    public Object getUserObject() {
        return userObject;
    }
}
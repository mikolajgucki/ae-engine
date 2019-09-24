package com.andcreations.eclipse;

/**
 * @author Mikolaj Gucki
 */
public class LinkedResource {
    /** The resource name. */
    private String name;
    
    /** The resource type. */
    private int type;
    
    /** The location. */
    private String location;
    
    /**
     * Constructs a {@link LinkedResource}.
     *
     * @param name The resource name.
     * @param type The resource type.
     * @param location The location.
     */
    public LinkedResource(String name,int type,String location) {
        this.name = name;
        this.type = type;
        this.location = location;
    }
    
    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the type.
     *
     * @return The type.
     */
    public int getType() {
        return type;
    }
    
    /**
     * Gets the location.
     *
     * @return The location.
     */
    public String getLocation() {
        return location;
    }
}
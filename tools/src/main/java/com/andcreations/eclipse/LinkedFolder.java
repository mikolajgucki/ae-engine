package com.andcreations.eclipse;

/**
 * @author Mikolaj Gucki
 */
public class LinkedFolder extends LinkedResource {
    /** */
    public static final int LINKED_FOLDER_TYPE = 2;
    
    /**
     * Constructs a {@link LinkedFolder}.
     *
     * @param name The resource name.
     * @param location The location.
     */
    public LinkedFolder(String name,String location) {
        super(name,LINKED_FOLDER_TYPE,location);
    }
}
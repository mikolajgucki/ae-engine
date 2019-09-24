package com.andcreations.ae.doc;

/**
 * Represents a group of navigation entries.
 *
 * @author Mikolaj Gucki
 */
public class NavGroup {
    /** The entry title. */
    private String title;    
    
    /** The group entries. */
    private NavEntry[] entries;
    
    /**
     * Gets the group title.
     *
     * @return The group title.
     */
    public String getTitle() {
        return title;
    }    
    
    /**
     * Gets the entries.
     *
     * @return The entries.
     */
    public NavEntry[] getEntries() {
        return entries;
    }
}
package com.andcreations.ae.doc;

/**
 * @author Mikolaj Gucki
 */
public class DocCfg {
    /** The documentation title. */
    private String title;
    
    /** The navigation. */
    private NavGroup[] navigation;
            
    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    } 
    
    /**
     * Gets the navigation.
     *
     * @return The navigation.
     */
    public NavGroup[] getNavigation() {
        return navigation;
    }
}
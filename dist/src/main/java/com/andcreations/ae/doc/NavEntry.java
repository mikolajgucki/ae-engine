package com.andcreations.ae.doc;

/**
 * Represents a navigation entry.
 *
 * @author Mikolaj Gucki
 */
public class NavEntry {
    /** The entry title. */
    private String title;
    
    /** The document identifier. */
    private String doc;
    
    /** The URL. */
    private String url;
    
    /**
     * Gets the entry title.
     *
     * @return The entry title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the document identifier.
     *
     * @return The document identifier.
     */
    public String getDoc() {
        return doc;
    }
    
    /**
     * Gets the URL.
     *
     * @return The URL.
     */
    public String getURL() {
        return url;
    }
    
    /**
     * Sets the URL.
     *
     * @param url The URL.
     */
    public void setURL(String url) {
        this.url = url;
    }
}
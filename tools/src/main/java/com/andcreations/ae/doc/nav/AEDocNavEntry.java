package com.andcreations.ae.doc.nav;

import java.util.Map;

/**
 * @author Mikolaj Gucki
 */
public class AEDocNavEntry {
    /** */
    private String text;
    
    /** */
    private Map<String,String> properties;
    
    /** */
    private int level;
    
    /** */
    AEDocNavEntry(int level,String text,Map<String,String> properties) {
        this.level = level;
        this.text = text;
        this.properties = properties;
    }
 
    /** */
    public int getLevel() {
        return level;
    }
    
    /** */
    public String getText() {
        return text;
    }
    
    /** */
    public String getTitle() {
        return properties.get("title");
    }
    
    /** */
    public String getLink() {
        return properties.get("link");
    }
}
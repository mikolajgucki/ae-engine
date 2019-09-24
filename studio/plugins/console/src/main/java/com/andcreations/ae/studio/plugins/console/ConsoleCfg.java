package com.andcreations.ae.studio.plugins.console;


/**
 * @author Mikolaj Gucki
 */
public class ConsoleCfg {
    /** */
    private String title;
    
    /** */
    private String iconName;
    
    /** */
    public ConsoleCfg(String title,String iconName) {
        this.title = title;
        this.iconName = iconName;
    }
    
    /**
     * Gets the console title.
     *
     * @return The console title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets the icon name.
     *
     * @return The icon name.
     */
    public String getIconName() {
        return iconName;
    }
}
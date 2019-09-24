package com.andcreations.ae.luadoc;

/**
 * The configuration of the LuaDoc generator.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocGeneratorCfg {
    /** The document title. */
    private String title;
    
    /**
     * Sets the title.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the title.
     */
    public String getTitle() {
        return title;
    }
}
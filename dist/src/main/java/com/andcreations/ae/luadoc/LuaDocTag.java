package com.andcreations.ae.luadoc;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

/**
 * Represents a LuaDoc comment tag along with its value.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocTag {
    /** The name of the brief description tag. */
    public static final String BRIEF = "brief";
    
    /** The name of the full description tag. */
    public static final String FULL = "full";
    
    /** The tag name. */
    private String name;
    
    /** The tag value. */
    private String value;
    
    /**
     * Constructs a {@link LuaDocTag}.
     *
     * @param name The tag name.
     * @param value The tag value.
     */
    public LuaDocTag(String name,String value) {
        this.name = name;
        this.value = value;        
    }
    
    /**
     * Gets the tag name.
     *
     * @return The tag name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the tag value.
     *
     * @return The tag value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Gets the tag markdown-processed value.
     *
     * @return The markdown-processed value.
     * @throws LuaDocException if the markdown processing fails.
     */
    public String getMarkdownValue() throws LuaDocException{
        if (value == null) {
            return null;
        }
        
        try {
            return new Markdown4jProcessor().process(value);
        } catch (IOException exception) {
            throw new LuaDocException(exception.getMessage(),exception);
        }
    }
}
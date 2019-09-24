package com.andcreations.ae.lua.parser;

/**
 * Represents a Lua element.
 *
 * @author Mikolaj Gucki
 */
public abstract class LuaElement {
    /** The begin line. */
    private int beginLine;
    
    /** The end line. */
    private int endLine;

    /** */
    protected LuaElement(int beginLine,int endLine) {
        this.beginLine = beginLine;
        this.endLine = endLine;
    }
    
    /**
     * Gets the number of the line at which the function begins.
     *
     * @return The begin line number.
     */
    public int getBeginLine() {
        return beginLine;
    }
    
    /**
     * Gets the numbe rof line line at which the function ends.
     *
     * @return The end line number.
     */
    public int getEndLine() {
        return endLine;
    }   
    
    /** */
    @Override
    public String toString() {
        return String.format("%d-%d",beginLine,endLine);
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof LuaElement) == false) {
            return false;
        }
        
        LuaElement that = (LuaElement)obj;
        return beginLine == that.beginLine && endLine == that.endLine;
    }
}
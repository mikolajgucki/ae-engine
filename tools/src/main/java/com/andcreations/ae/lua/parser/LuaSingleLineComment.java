package com.andcreations.ae.lua.parser;

/**
 * @author Mikolaj Gucki
 */
public class LuaSingleLineComment extends LuaComment {
    /** */
    private String comment;
    
    /** */
    public LuaSingleLineComment(String comment,int line) {
        super(line,line);
        this.comment = comment;
    }    
    
    /** */
    public String getComment() {
        return comment;
    }
    
    /** */
    @Override
    public String toString() {
        return getComment();
    }
}
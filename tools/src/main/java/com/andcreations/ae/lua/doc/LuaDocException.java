package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaDocException extends Exception {
    /** */
    private int line;
    
    /** */
    LuaDocException(String message,int line) {
        super(message);
        this.line = line;
    }
    
    /** */
    LuaDocException(String message,Throwable cause,int line) {
        super(message);
        this.line = line;
    }
    
    /** */
    public int getLine() {
        return line;
    }
}
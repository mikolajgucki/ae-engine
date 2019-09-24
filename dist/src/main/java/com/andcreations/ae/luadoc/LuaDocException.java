package com.andcreations.ae.luadoc;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaDocException extends Exception {
    /** */
    LuaDocException() {
    }
    
    /** */    
    LuaDocException(String message) {
        super(message);
    }
    
    /** */
    LuaDocException(String message,Throwable cause) {
        super(message,cause);
    }
}
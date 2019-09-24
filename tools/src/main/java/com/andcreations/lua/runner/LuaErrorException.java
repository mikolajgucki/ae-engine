package com.andcreations.lua.runner;

import java.io.IOException;

/**
 * Thrown when it failed to run a Lua script.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaErrorException extends IOException {
    /** The error message. */
    private String error;
    
    /** */
    public LuaErrorException() {
    }
    
    /** */
    public LuaErrorException(String message) {
        super(message);
    }
    
    /** */
    public LuaErrorException(String message,Throwable cause) {
        super(message,cause);
    }
    
    /** */
    public void setError(String error) {
        this.error = error;
    }
    
    /**
     * Gets the error message.
     *
     * @return The error message.
     */
    public String getError() {
        return error;
    }
}
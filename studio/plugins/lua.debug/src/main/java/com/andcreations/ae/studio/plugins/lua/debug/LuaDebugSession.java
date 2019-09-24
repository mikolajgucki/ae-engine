package com.andcreations.ae.studio.plugins.lua.debug;

/**
 * @author Mikolaj Gucki
 */
public interface LuaDebugSession {
    /** */
    void stepInto();
    
    /** */
    void stepOver();
    
    /** */
    void stepReturn();
    
    /** */
    void continueExecution();
    
    /** */
    void runLuaSnippet(String code);
}
package com.andcreations.ae.studio.plugins.lua.debug;

/**
 * @author Mikolaj Gucki
 */
interface LuaDebugMenuListener {
    /** */
    void stepInto();
    
    /** */
    void stepOver();
    
    /** */
    void stepReturn();
    
    /** */
    void continueExecution();
}
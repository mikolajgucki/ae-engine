package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

/**
 * @author Mikolaj Gucki
 */
public interface LuaBreakpointsListener {
    /**
     * Called when a breakpoint has been added.
     *
     * @param breakpoint The breakpoint.
     * @return <code>true</code> on success, <code>false</code> otherwise.
     */
    boolean breakpointAdded(LuaBreakpoint breakpoint);
        
    /**
     * Called when a breakpoint has been removed.
     *
     * @param breakpoint The breakpoint.
     * @return <code>true</code> on success, <code>false</code> otherwise.
     */
    boolean breakpointRemoved(LuaBreakpoint breakpoint); 
    
    /**
     * Called when breakpoint state has been removed.
     *
     * @param breakpoint The breakpoint.
     * @return <code>true</code> on success, <code>false</code> otherwise.
     */
    boolean breakpointStateChanged(LuaBreakpoint breakpoint);
    
    /**
     * Called when a breakpoint list has changed.
     *
     * @param breakpoint The breakpoint.
     * @param oldLine The old line.
     */
    boolean breakpointLineChanged(LuaBreakpoint breakpoint,int oldLine);
}
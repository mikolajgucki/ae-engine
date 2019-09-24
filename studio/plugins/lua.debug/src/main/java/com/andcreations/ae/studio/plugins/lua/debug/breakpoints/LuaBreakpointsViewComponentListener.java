package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

/**
 * @author Mikolaj Gucki
 */
interface LuaBreakpointsViewComponentListener {
    /** */
    void breakpointStateChanged(LuaBreakpoint breakpoint);
    
    /** */
    void removeBreakpoint(LuaBreakpoint breakpoint);
}
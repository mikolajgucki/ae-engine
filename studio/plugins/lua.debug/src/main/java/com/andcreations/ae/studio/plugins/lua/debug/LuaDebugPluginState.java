package com.andcreations.ae.studio.plugins.lua.debug;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugin.PluginState;

/**
 * @author Mikolaj Gucki
 */
public class LuaDebugPluginState extends PluginState {
    /** */
    private List<LuaBreakpointState> breakpoints;
    
    /** */
    private String luaSnippet;
    
    /** */
    public List<LuaBreakpointState> getBreakpoints() {
        if (breakpoints == null) {
            breakpoints = new ArrayList<>();
        }
        
        return breakpoints;
    } 
    
    /** */
    public void setBreakpoints(List<LuaBreakpointState> breakpoints) {
        this.breakpoints = breakpoints;
    }
    
    /** */
    public String getLuaSnippet() {
        if (luaSnippet == null) {
            luaSnippet = "";
        }
        
        return luaSnippet;
    }
    
    /** */
    public void setLuaSnippet(String luaSnippet) {
        this.luaSnippet = luaSnippet;
    }
}
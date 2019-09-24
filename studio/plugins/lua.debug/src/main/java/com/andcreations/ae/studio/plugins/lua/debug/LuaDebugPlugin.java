package com.andcreations.ae.studio.plugins.lua.debug;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.lua.debug.breakpoints.LuaBreakpoint;
import com.andcreations.ae.studio.plugins.lua.debug.breakpoints.LuaBreakpoints;
import com.andcreations.ae.studio.plugins.lua.debug.globals.Globals;
import com.andcreations.ae.studio.plugins.lua.debug.snippet.LuaSnippet;
import com.andcreations.ae.studio.plugins.lua.debug.snippet.LuaSnippetListener;
import com.andcreations.ae.studio.plugins.lua.debug.traceback.Traceback;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.text.editor",
    "com.andcreations.ae.studio.plugins.lua"
})
public class LuaDebugPlugin extends Plugin<LuaDebugPluginState> {
    /** */
    private LuaDebugPluginState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        LuaDebugIcons.init();
        Traceback.get().init();
        Globals.get().init();
        initLuaSnippet();
        LuaDebug.get().init();
        LuaBreakpoints.get().init(getState().getBreakpoints());
    }
    
    /** */
    @Override   
    public void stop() throws PluginException {
        dumpBreakpointsState();
    }
    
    /** */
    private void initLuaSnippet() {
        LuaSnippet.get().init(getState(),new LuaSnippetListener() {
            /** */
            @Override
            public void runLuaSnippet(String code) {
                LuaDebug.get().runLuaSnippet(code);
            }
        });
    }
    
    /** */
    private void dumpBreakpointsState() {
        LuaDebugPluginState state = getState();
        
        List<LuaBreakpointState> breakpointsState = new ArrayList<>();
        state.setBreakpoints(breakpointsState);
        
        for (LuaBreakpoint breakpoint:LuaBreakpoints.get().all()) {
            breakpointsState.add(new LuaBreakpointState(breakpoint.getSource(),
                breakpoint.getLine(),breakpoint.isEnabled()));
        }
    }
    
    /** */
    @Override
    public LuaDebugPluginState getState() {
        if (state == null) {
            state = new LuaDebugPluginState();
        }
        
        return state;
    }
    
    /** */
    @Override
    public void setState(LuaDebugPluginState state) {
        this.state = state;
    }    
}
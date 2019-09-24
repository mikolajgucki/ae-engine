package com.andcreations.ae.studio.plugins.lua.debug;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class LuaDebugIcons {
    public static final String LUA_BREAKPOINT = "lua_breakpoint";
    public static final String LUA_BREAKPOINTS = "lua_breakpoints";    
    public static final String CONTINUE_EXECUTION = "continue_execution";    
    public static final String STEP_INTO = "step_into";
    public static final String STEP_OVER = "step_over";
    public static final String STEP_RETURN = "step_return";
    public static final String TRACEBACK = "traceback";
    public static final String GLOBALS = "globals";
    public static final String LUA_SNIPPET_RUN = "lua_snippet_run";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(LuaDebugIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(LUA_BREAKPOINT,Icons.blue());
        Icons.colorize(LUA_BREAKPOINTS,Icons.blue());
        Icons.colorize(CONTINUE_EXECUTION,Icons.blue());
        Icons.colorize(STEP_INTO,Icons.blue());
        Icons.colorize(STEP_OVER,Icons.blue());
        Icons.colorize(STEP_RETURN,Icons.blue());
        Icons.colorize(TRACEBACK,Icons.blue());
        Icons.colorize(GLOBALS,Icons.blue());
        Icons.colorize(LUA_SNIPPET_RUN,Icons.green());
    }
}
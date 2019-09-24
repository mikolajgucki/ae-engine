package com.andcreations.ae.studio.plugins.lua.explorer;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
class LuaExplorerIcons {
    public static final String LUA_EXPLORER = "lua_explorer";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(LuaExplorerIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(LUA_EXPLORER,Icons.lightGray());
    }
}
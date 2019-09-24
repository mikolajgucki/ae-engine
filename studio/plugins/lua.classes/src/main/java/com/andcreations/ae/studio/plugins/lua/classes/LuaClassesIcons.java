package com.andcreations.ae.studio.plugins.lua.classes;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class LuaClassesIcons {
    /** */
    public static final String LUA_CLASS = "lua_class";    
    
    /** */
    public static final String LUA_PROJECT_CLASS = "lua_project_class";    
    
    /** */
    public static final String LUA_HIERARCHY = "lua_hierarchy";    
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(LuaClassesIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(LUA_CLASS,Icons.darkGray());
        Icons.colorize(LUA_PROJECT_CLASS,Icons.lightGray());
        Icons.colorize(LUA_HIERARCHY,Icons.orange());        
    }
}
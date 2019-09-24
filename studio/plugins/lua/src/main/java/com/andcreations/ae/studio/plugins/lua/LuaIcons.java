package com.andcreations.ae.studio.plugins.lua;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class LuaIcons {
    /** */
    public static final String LUA_FILE = "lua_file";
    
    /** */
    public static final String LUA_PROJECT_FILE = "lua_project_file";    
    
    /** */
    public static final String LUA_FUNC = "lua_func";
    
    /** */
    public static final String LUA_FUNC_LOCAL = "lua_func_local";
    
    /** */
    public static final String LUA_ASSIGNMENT = "lua_assignment";
    
    /** */
    public static final String LUA_ASSIGNMENT_LOCAL = "lua_assignment_local";
    
    /** */
    public static final String LUA_TABLE_FIELD = "lua_table_field";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(LuaIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(LUA_FILE,Icons.darkGray());
        Icons.colorize(LUA_PROJECT_FILE,Icons.lightGray());
        Icons.colorize(LUA_FUNC,Icons.green());
        Icons.colorize(LUA_FUNC_LOCAL,Icons.red());
        Icons.colorize(LUA_ASSIGNMENT,Icons.green());
        Icons.colorize(LUA_ASSIGNMENT_LOCAL,Icons.red());
        Icons.colorize(LUA_TABLE_FIELD,Icons.lightGray());
    
    // decorated
        Icons.getIcon(LuaIcons.LUA_FILE,DefaultIcons.DECO_ERROR);
        Icons.getIcon(LuaIcons.LUA_FILE,DefaultIcons.DECO_WARNING);
    }
}
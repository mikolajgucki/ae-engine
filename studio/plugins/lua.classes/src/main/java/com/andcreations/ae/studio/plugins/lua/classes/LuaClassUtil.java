package com.andcreations.ae.studio.plugins.lua.classes;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * Provides Lua class-related utility methods.
 * 
 * @author Mikolaj Gucki
 */
public class LuaClassUtil {
    /** */
    public static ImageIcon getLuaClassIcon(LuaClass luaClass) {
        if (luaClass == null) {
            return null;
        }
        
        if (ProjectFiles.get().isProjectFile(luaClass.getFile())) {
            return Icons.getIcon(LuaClassesIcons.LUA_PROJECT_CLASS);                
        }
        return Icons.getIcon(LuaClassesIcons.LUA_CLASS);
    }
}

package com.andcreations.ae.studio.plugins.lua.classes;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.lua.classes.hierarchy.LuaHierarchy;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.file.problems",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.lua.parser",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.text.editor"
})
public class LuaClassesPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        LuaClassesIcons.init();
        LuaClasses.get().init();
        
    // hierarchy
        LuaHierarchy.get().init();
    }
}
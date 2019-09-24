package com.andcreations.ae.studio.plugins.lua.test;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.ant",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.lua.explorer",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files"
})
public class LuaTestPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
    // view
        LuaTestView luaTestView = new LuaTestView();
        luaTestView.init();
        
    // main
        LuaTest.get().init(luaTestView);
        LuaTest.get().addLuaTestListener(luaTestView);
        
        luaTestView.setLuaTestViewListener(LuaTest.get());
    }
}
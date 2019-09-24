package com.andcreations.ae.studio.plugins.lua.lib;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.lua.lib.renderer.LuaElementRenderer;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.problems"
})
public class LuaLibPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        LuaElementRenderer.init();
        NativeLua.loadLib();
    }
}
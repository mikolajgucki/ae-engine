package com.andcreations.ae.studio.plugins.lua;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.text.editor"
})
public class LuaPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        LuaIcons.init();
        LuaFile.init();
    }
}
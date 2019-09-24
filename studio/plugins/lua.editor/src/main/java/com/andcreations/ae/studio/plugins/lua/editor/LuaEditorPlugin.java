package com.andcreations.ae.studio.plugins.lua.editor;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.lua.lib",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.text.editor"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.project"
})
public class LuaEditorPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        LuaUTF8.get().init();
    }
}
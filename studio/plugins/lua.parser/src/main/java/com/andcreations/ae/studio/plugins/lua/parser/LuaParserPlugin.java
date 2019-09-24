package com.andcreations.ae.studio.plugins.lua.parser;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.lua.lib",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.text.editor"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.file.problems",
    "com.andcreations.ae.studio.plugins.project"
})
public class LuaParserPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
    // start parser
        LuaParser.get().start();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
    // stop parser
        LuaParser.get().stop();
    }
}
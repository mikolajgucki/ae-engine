package com.andcreations.ae.studio.plugins.lua.autocompletion;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc.CApiAutocompletion;
import com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc.LuaCommentAutocompletion;
import com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc.LuaDocAutocompletion;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.text.editor",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.lua.parser"
})
public class LuaAutocompletionPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        PredefinedAutocompletion.get().init();
        LuaAutocompletion.get().init();
        LuaCommentAutocompletion.get().init();
        CApiAutocompletion.get().init();        
        LuaDocAutocompletion.get().init();
    }
}
package com.andcreations.ae.studio.plugins.outline;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * The plugins which displays outline of a file.
 * 
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.file"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.text.editor"
})
public class OutlinePlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        OutlineIcons.init();
        Outline.get().init();
    }
}
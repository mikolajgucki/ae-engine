package com.andcreations.ae.plugin.ui.gpu.studio.plugin;

import java.io.IOException;

import com.andcreations.resources.ResourceLoader;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.LayoutViewer;
import com.andcreations.ae.plugin.ui.gpu.studio.plugin.resources.R;

/**
 * @author Mikolaj Gucki
 */
class GPULayoutViewer {
    /** */
    static void init() throws PluginException {
    // load snippet
        try {
            LayoutViewer.get().addLoadLuaSnippet(
                ResourceLoader.loadAsString(R.class,"modules.lua"));
        } catch (IOException exception) {
            throw new PluginException(exception);
        }
    }
}

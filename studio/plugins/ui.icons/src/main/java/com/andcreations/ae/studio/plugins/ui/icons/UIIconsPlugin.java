package com.andcreations.ae.studio.plugins.ui.icons;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginState;

/**
 * @author Mikolaj Gucki
 */
public class UIIconsPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() {
        DefaultIcons.init();
    }
}
package com.andcreations.ae.plugin.ui.gpu.studio.plugin;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfter;

/**
 * @author Mikolaj Gucki
 */
@StartAfter(id={
    "com.andcreations.ae.plugin.ui.studio.plugin"
})
public class UIGPUPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        GPULayoutViewer.init();
    }
}
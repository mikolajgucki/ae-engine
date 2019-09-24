package com.andcreations.ae.studio.plugins.simulator.resources;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.resources.Resources;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.text.editor"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.resources"
})
public class SimulatorResourcesPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        Resources.get().addSource(new SimulatorResourceSource());
    }
}
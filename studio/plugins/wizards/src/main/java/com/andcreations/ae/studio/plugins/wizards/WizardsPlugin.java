package com.andcreations.ae.studio.plugins.wizards;

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
    "com.andcreations.ae.studio.plugins.ui.main"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.project"
})
public class WizardsPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        Wizards.get().init();
    }
}
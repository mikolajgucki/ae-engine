package com.andcreations.ae.studio.plugins.ui.common;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.ui.common.blinker.Blinker;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons"
})
public class UICommonPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        UIColors.init();
        Blinker.get().start();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        Blinker.get().stop();
    }
}
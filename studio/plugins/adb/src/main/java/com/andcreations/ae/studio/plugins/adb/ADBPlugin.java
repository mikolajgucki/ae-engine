package com.andcreations.ae.studio.plugins.adb;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.android"
})
public class ADBPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        ADBIcons.init();
        ADBDevices.get().init();
        LogCat.get().init();
        ADBWrapper.get().init();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        ADBWrapper.get().stop();
        LogCat.get().stop();
    }
}
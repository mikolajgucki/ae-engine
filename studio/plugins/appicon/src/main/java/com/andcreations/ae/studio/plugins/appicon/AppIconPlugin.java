package com.andcreations.ae.studio.plugins.appicon;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.project.files"
})
public class AppIconPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        AppIcon.get().init();
    }
}
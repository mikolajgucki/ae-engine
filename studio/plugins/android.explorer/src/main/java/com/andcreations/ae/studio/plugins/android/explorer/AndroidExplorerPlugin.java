package com.andcreations.ae.studio.plugins.android.explorer;

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
    "com.andcreations.ae.studio.plugins.file.explorer",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.android"
})
public class AndroidExplorerPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        AndroidExplorerIcons.init();
        
        AndroidExplorerInit androidExplorerInit = new AndroidExplorerInit();
        androidExplorerInit.init();
    }
}

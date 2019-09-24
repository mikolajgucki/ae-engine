package com.andcreations.ae.studio.plugins.file;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfter;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main"
})
@StartAfter(id={
    // Start after selecting the project to store the File plugin state
    // in the project directory.
    "com.andcreations.ae.studio.plugins.project.select"    
})
public class FilePlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        Files.get().init(getStorageDir());
    }
}
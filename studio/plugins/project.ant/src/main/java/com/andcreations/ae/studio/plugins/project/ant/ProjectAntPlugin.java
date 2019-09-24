package com.andcreations.ae.studio.plugins.project.ant;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ant",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.builders"
})
public class ProjectAntPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        ProjectAnt.get().init();
    }
}
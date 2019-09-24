package com.andcreations.ae.studio.plugins.project.explorer;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.file.explorer",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.appicon",
    "com.andcreations.ae.studio.plugins.ui.main"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.file"
})
public class ProjectExplorerPlugin extends Plugin<PluginState> {
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.project")
    private Object projectPluginObject;    
    
    /** */
    @Override
    public void start() throws PluginException {
        ProjectExplorerInit projectExplorerInit = new ProjectExplorerInit();
        projectExplorerInit.init(projectPluginObject);
    }
}


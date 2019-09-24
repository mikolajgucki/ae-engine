package com.andcreations.ae.studio.plugins.project.select;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugin.StartBeforeAndRequire;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.ae.dist"
})
@StartBeforeAndRequire(id={
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files"
})
@RequiredPlugins(id={
})
public class ProjectSelectPlugin extends Plugin<PluginState> {
    /* */
    @Required(id="com.andcreations.ae.studio.plugins.project")
    private Object projectCorePlugin;

    /** */
    private ProjectSelectState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        state = restoreAppState(ProjectSelectState.class);
        if (state == null) {
            state = new ProjectSelectState();
        }
        
        UICommon.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ProjectSelect select = new ProjectSelect(
                    state,projectCorePlugin);
                select.run();
            }
        });
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        storeAppState(ProjectSelectState.class,state);
    }
}
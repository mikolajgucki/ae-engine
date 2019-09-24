package com.andcreations.ae.studio.plugins.ant;

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
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.problems"
})
public class AntPlugin extends Plugin<PluginState> {
    /** */
    private AntAppState appState;
    
    /** */
    @Override
    public void start() throws PluginException {
    // application-wide state
        appState = restoreAppState(AntAppState.class);
        if (appState == null) {
            appState = new AntAppState();
        }        
        
    // initialize
        AntIcons.init();
        AntPreferences.get().init(appState);
        Ant.get().init();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        storeAppState(AntAppState.class,appState);
    }    
}
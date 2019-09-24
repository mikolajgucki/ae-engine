package com.andcreations.ae.studio.plugins.builders;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main"
}) 
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.console"
})
public class BuildersPlugin extends Plugin<PluginState> {
	/** */
	private BuildersAppState appState;
	
    /** */
    @Override
    public void start() throws PluginException {
	// restore application state
    	appState = restoreAppState(BuildersAppState.class);
    	if (appState == null) {
    		appState = new BuildersAppState();
    	}
    	
	// initialize
        BuildersIcons.init();
        BuildersPreferences.get().init(appState);
        Builders.get().init();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        Builders.get().finish();
        storeAppState(BuildersAppState.class,appState);
    }    
}
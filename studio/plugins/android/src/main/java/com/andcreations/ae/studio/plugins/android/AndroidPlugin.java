package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.ant",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.resources",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.builders",
    "com.andcreations.ae.studio.plugins.text.editor"
})
public class AndroidPlugin extends Plugin<AndroidPluginState> {
    /** */
    private AndroidAppState appState;
    
    /** */
    private AndroidPluginState state;
    
    /** */
    @Override
    public void start() throws PluginException {
    // application-wide state
        appState = restoreAppState(AndroidAppState.class);
        if (appState == null) {
            appState = new AndroidAppState();
        }
            
    // initialize
        AndroidIcons.init();
        AndroidProjectDir.get().init(state);
        AndroidSettings.get().init(state);
        AndroidPreferences.get().init(appState);
        AndroidBuilders.appendBuilders(state);
        AndroidResources.init();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        storeAppState(AndroidAppState.class,appState);
    }
    
    /** */
    @Override
    public void setState(AndroidPluginState state) {
        if (state == null) {
            state = new AndroidPluginState();
        }
        this.state = state;
    }
    
    /** */
    @Override
    public AndroidPluginState getState() {
        return state;
    }
}
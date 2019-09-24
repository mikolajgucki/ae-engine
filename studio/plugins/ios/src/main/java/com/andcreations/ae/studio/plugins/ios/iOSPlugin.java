package com.andcreations.ae.studio.plugins.ios;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.builders.Builders;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.console",    
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.builders"
})
public class iOSPlugin extends Plugin<iOSPluginState> {
    /** */
    private iOSPluginState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        iOSIcons.init();
        iOSSettings.get().init(state);
        appendBuilders();
    }
    
    /** */
    private void appendBuilders() {
        Builders.get().appendBuilder(new iOSProjectUpdateBuilder(state));
        Builders.get().appendBuilder(new iOSProjectCleanBuilder(state));
    }
    
    /** */
    @Override
    public void setState(iOSPluginState state) {
        if (state == null) {
            state = new iOSPluginState();
        }
        this.state = state;
    }
    
    /** */
    @Override
    public iOSPluginState getState() {
        return state;
    }    
}
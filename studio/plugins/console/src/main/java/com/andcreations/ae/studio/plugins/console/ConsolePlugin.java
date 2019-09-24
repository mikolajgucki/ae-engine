package com.andcreations.ae.studio.plugins.console;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.StartAfter;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.search.common"
})
@StartAfter(id={
    // Start after selecting the project to store the Console plugin state
    // in the project directory.
    "com.andcreations.ae.studio.plugins.project.select"
})
public class ConsolePlugin extends Plugin<ConsolePluginState> {
    /** */
    private ConsolePluginState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        ConsoleIcons.init();
        DefaultConsole.get().init();
    }
    
    /** */
    @Override
    public ConsolePluginState getState() {
        if (state == null) {
            state = new ConsolePluginState();
        }
        return state;
    }
    
    /** */
    @Override
    public void setState(ConsolePluginState state) {
        this.state = state;
    }    
}
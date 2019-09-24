package com.andcreations.ae.plugin.ui.studio.plugin;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.StartAfter;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.LayoutViewer;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.LayoutSettings;

/**
 * @author Mikolaj Gucki
 */
@StartAfter(id={
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.lua.lib",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.text.editor",
    "com.andcreations.ae.studio.plugins.simulator"
})
public class UIPlugin extends Plugin<UIPluginState> {
    /** The plugin state. */
    private UIPluginState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        UIIcons.init();
        LayoutSettings.get().init(state.getLayoutViewerState());
        LayoutViewer.get().init(state.getLayoutViewerState());
    }
    
    /** */
    @Override
    public UIPluginState getState() {
        return state;
    }
    
    /** */
    @Override
    public void setState(UIPluginState state) {
        if (state == null) {
            state = new UIPluginState();
        }
        this.state = state;
    }    
}
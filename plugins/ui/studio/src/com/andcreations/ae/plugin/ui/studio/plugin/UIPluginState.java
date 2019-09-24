package com.andcreations.ae.plugin.ui.studio.plugin;

import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.LayoutViewerState;

/**
 * @author Mikolaj Gucki
 */
public class UIPluginState extends PluginState {
    /** */
    private LayoutViewerState layoutViewerState = new LayoutViewerState();
    
    /** */
    public void setLayoutViewerState(LayoutViewerState layoutViewerState) {
        this.layoutViewerState = layoutViewerState;
    }
    
    /** */
    public LayoutViewerState getLayoutViewerState() {
        return layoutViewerState;
    }
}
package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.project.ProjectSettings;
import com.andcreations.ae.plugin.ui.studio.plugin.UIPluginState;

/**
 * @author Mikolaj Gucki
 */
public class LayoutSettings {
    /** */
    private static LayoutSettings instance;
    
    /** */
    private LayoutViewerState state;
    
    /** */
    public void init(final LayoutViewerState state) {
        this.state = state;
        
    // settings page
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                ProjectSettings.get().addPage(new LayoutSettingsPage(state));
            }
        });        
    }
    
    /** */
    public static LayoutSettings get() {
        if (instance == null) {
            instance = new LayoutSettings();
        }
        
        return instance;
    }
}
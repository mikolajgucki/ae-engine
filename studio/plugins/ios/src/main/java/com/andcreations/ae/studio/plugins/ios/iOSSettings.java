package com.andcreations.ae.studio.plugins.ios;

import com.andcreations.ae.studio.plugins.project.ProjectSettings;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
class iOSSettings {
    /** */
    private static iOSSettings instance;
    
    /** */
    void init(final iOSPluginState state) {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {    
                ProjectSettings.get().addPage(new iOSSettingsPage(state));
            }
        });
    }
    
    /** */
    static iOSSettings get() {
        if (instance == null) {
            instance = new iOSSettings();
        }
        
        return instance;
    }
}

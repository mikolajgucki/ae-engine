package com.andcreations.ae.studio.plugins.ui.main;

import bibliothek.gui.DockController;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.preferences.Preferences;

/**
 * The UIMain plugin. The plugin state is stored in
 * {@link UIMainFinalizerPlugin} and this is to keep the state in the project
 * directory.
 * 
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons"
})
public class UIMainPlugin extends Plugin<PluginState> {
    @Required(id="com.andcreations.ae.studio.plugins.ui.common")
    private Object uiCommonPlugin;
    
    /** */
    @Override
    public void start() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @SuppressWarnings("deprecation")
            public void run() {
                DockController.disableCoreWarning();
                MainFrame.create();
                Preferences.get().init();
            }
        });
    }
    
    /** */
    @Override
    public void stop() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            public void run() {
                MainFrame.get().stop();
            }
        });
    }
}

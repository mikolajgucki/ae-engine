package com.andcreations.ae.studio.plugins.ui.main.preferences;

import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsDialog;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Preferences {
    /** */
    private static Preferences instance;
    
    /** */
    private final BundleResources res = new BundleResources(Preferences.class);       
    
    /** */
    private SettingsDialog dialog;
    
    /** */
    public void init() {
        dialog = new SettingsDialog(MainFrame.get(),res.getStr("title"));
        PluginManager.addPluginFinalizer(new Runnable() {
            /** */
            @Override
            public void run() {
                UICommon.invokeAndWait(new Runnable() {
                    /** */
                    @Override
                    public void run() {
                        dialog.create();
                    }
                });                
            }
        });        
    }
    
    /** */
    public void open() {
        dialog.showSettingsDialog();
    }
    
    /** */
    public void addPage(PreferencesPage page) {
        dialog.addPage(page);
    }
    
    /** */
    public static Preferences get() {
        if (instance == null) {
            instance = new Preferences();
        }
        
        return instance;
    }
}
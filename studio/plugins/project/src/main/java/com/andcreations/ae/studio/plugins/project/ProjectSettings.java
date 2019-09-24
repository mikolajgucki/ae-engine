package com.andcreations.ae.studio.plugins.project;

import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsDialog;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * The project settings.
 *
 * @author Mikolaj Gucki
 */
public class ProjectSettings {
    /** */
    private static ProjectSettings instance;
    
    /** */
    private final BundleResources res =
        new BundleResources(ProjectSettings.class);     
    
    /** */
    private SettingsDialog dialog;    
    
    /** */
    void init() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                dialog = new SettingsDialog(
                    MainFrame.get(),res.getStr("title"));
            }
        });
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
    void open() {
        dialog.showSettingsDialog();
    }
    
    /** */
    public void addPage(ProjectSettingsPage page) {
        dialog.addPage(page);
    }    
    
    /** */
    public static ProjectSettings get() {
        if (instance == null) {
            instance = new ProjectSettings();
        }
        
        return instance;
    }
}
package com.andcreations.ae.studio.plugins.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * The project menu.
 *
 * @author Mikolaj Gucki 
 */
@SuppressWarnings("serial")
public class ProjectMenu extends JMenu {
    /** */
    private static ProjectMenu instance;
    
    /** */
    private final BundleResources res = new BundleResources(ProjectMenu.class);       
    
    /** */
    private ProjectMenu() {
        create();
    }
    
    /** */
    private void create() {        
        setText(res.getStr("menu.text"));
        setMnemonic(KeyEvent.VK_P);
        
    // settings
        PluginManager.addPluginFinalizer(new Runnable() {
            /** */
            @Override
            public void run() {
                UICommon.invokeAndWait(new Runnable() {
                    /** */
                    @Override
                    public void run() {
                        addMenuItems();
                    }
                });                
            }
        });        
    }
    
    /** */
    private void addMenuItems() {
        ProjectMenu.get().addSeparator();
        addCleanMenuItem();
        addSettingsMenuItem();
    }
    
    /** */
    private void addCleanMenuItem() {
        JMenuItem cleanMenu = new JMenuItem(res.getStr("clean"));
        cleanMenu.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                ProjectCleaner.cleanProject();
            }
        });
        ProjectMenu.get().add(cleanMenu);
        
    }
    
    /** */
    private void addSettingsMenuItem() {
        JMenuItem settingsMenu = new JMenuItem(res.getStr("settings"));
        settingsMenu.setIcon(Icons.getIcon(DefaultIcons.SETTINGS));
        settingsMenu.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                ProjectSettings.get().open();
            }
        });
        ProjectMenu.get().add(settingsMenu);
    }    
    
    /** */
    void init() {
        MainFrame.get().getMainFrameMenuBar().add(this);
    }
    
    /** */
    public static ProjectMenu get() {
        if (instance == null) {
            UICommon.invokeAndWait(new Runnable() {
                /** */
                @Override
                public void run() {
                    instance = new ProjectMenu();
                }
            });
        }
        
        return instance;
    }
}
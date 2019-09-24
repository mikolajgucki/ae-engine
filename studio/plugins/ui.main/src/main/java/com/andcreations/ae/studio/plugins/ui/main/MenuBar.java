package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.preferences.Preferences;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.resources.BundleResources;

/**
 * The menu bar.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
    /** */
    private final BundleResources res = new BundleResources(MenuBar.class);    
    
    /** The file menu. */
    private JMenu fileMenu;
    
    /** */
    private ViewsMenu viewsMenu;
    
    /** */
    private WindowMenu windowMenu;
    
    /** */
    MenuBar(ViewManager viewManager) {
        create(viewManager);
    }
    
    private void createFileMenu() {
    // file
        fileMenu = new JMenu(res.getStr("file"));
        fileMenu.setMnemonic(KeyEvent.VK_F);
    
    // preferences
        JMenuItem preferences = new JMenuItem(res.getStr("preferences"));
        preferences.setIcon(Icons.getIcon(DefaultIcons.SETTINGS));
        fileMenu.add(preferences);
        preferences.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                Preferences.get().open();
            }            
        });    
        
    // restart
        JMenuItem restart = new JMenuItem(res.getStr("restart"));
        fileMenu.add(restart);
        restart.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                PluginManager.postStop(1);
            }            
        });
        restart.setMnemonic(KeyEvent.VK_Q);
        
    // quit
        JMenuItem quit = new JMenuItem(res.getStr("quit"));
        fileMenu.add(quit);
        quit.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                PluginManager.postStop();
            }            
        });
        quit.setMnemonic(KeyEvent.VK_Q);
    }
    
    /** */
    private void create(ViewManager viewManager) {
        createFileMenu();
        add(fileMenu);
        
        viewsMenu = new ViewsMenu();
        add(viewsMenu);
        
        windowMenu = new WindowMenu();
    }
    
    /**
     * Updates the views menu. This method should be called whenever the view
     * providers change.
     */
    public void updateViewsMenu() {
    	viewsMenu.updateOpenViewItems();
    }
    
    /** */
    void finalizeBeforeShowing() {
    	updateViewsMenu();
        add(windowMenu);
    }
    
    /** */
    public ViewsMenu getViewsMenu() {
        return viewsMenu;
    }
}

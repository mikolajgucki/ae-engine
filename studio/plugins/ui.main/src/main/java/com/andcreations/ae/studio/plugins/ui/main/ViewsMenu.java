package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.dialogs.GoToView;
import com.andcreations.resources.BundleResources;

/**
 * The views menu.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class ViewsMenu extends JMenu {
    /** */
    private final BundleResources res = new BundleResources(ViewsMenu.class);
    
    /** The open view menu. */
    private JMenu openViewMenu;
    
    /** The next view menu. */
    private JMenuItem nextViewMenu;
    
    /** The previous view menu. */
    private JMenuItem previousViewMenu;
    
    /** The go to view menu. */
    private JMenuItem goToViewMenu;
    
    /** */
    ViewsMenu() {
        create();
    }
    
    /** */
    private void create() {
        setText(res.getStr("views"));
        setMnemonic(KeyEvent.VK_V);
        
    // open
        openViewMenu = new JMenu(res.getStr("open.view"));
        add(openViewMenu);
        
        addSeparator();
    // next
        nextViewMenu = new JMenuItem(res.getStr("next.view"));
        nextViewMenu.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_RIGHT,KeyEvent.ALT_DOWN_MASK));
        nextViewMenu.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                MainFrame.get().getViewManager().goToNextView();
            }
        });        
        add(nextViewMenu);
        
    // previous    
        previousViewMenu = new JMenuItem(res.getStr("previous.view"));
        previousViewMenu.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_LEFT,KeyEvent.ALT_DOWN_MASK));
        previousViewMenu.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                MainFrame.get().getViewManager().goToPreviousView();
            }
        });        
        add(previousViewMenu);    
        
    // go to view
        goToViewMenu = new JMenuItem(res.getStr("go.to.view"));
        goToViewMenu.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_E,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        goToViewMenu.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                GoToView.go();
            }
        });        
        add(goToViewMenu);    
    }
    
    /** */
    private List<ViewProvider> getViewProviders() {
        ViewManager viewManager = MainFrame.get().getViewManager();
        List<ViewProvider> viewProviders =
            new ArrayList<>(viewManager.getViewProviders());
        Collections.sort(viewProviders,new Comparator<ViewProvider>() {
            /** */
            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
            
            /** */
            @Override
            public int compare(ViewProvider a,ViewProvider b) {
                return a.getTitle().compareToIgnoreCase(b.getTitle());
            }
        });
        
        return viewProviders;
    }
    
    /** */
    void updateOpenViewItems() {
    	openViewMenu.removeAll();
        List<ViewProvider> viewProviders = getViewProviders();
        
        for (final ViewProvider viewProvider:viewProviders) {
            JMenuItem viewItem = new JMenuItem(
                viewProvider.getTitle(),viewProvider.getIcon());
            viewItem.addActionListener(new ActionListener() {
                /** */
                @Override
                public void actionPerformed(ActionEvent event) {
                    viewProvider.showView();
                }
            });
            openViewMenu.add(viewItem);
        }
    }
}
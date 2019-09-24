package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.resources.BundleResources;

/**
 * The window menu.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class WindowMenu extends JMenu {
    /** */
    private final BundleResources res = new BundleResources(WindowMenu.class);
    
    /** */
    WindowMenu() {
        create();
    }
    
    /** */
    private void create() {
        setText(res.getStr("window"));
        setMnemonic(KeyEvent.VK_W);    
        
    // restore
        JMenuItem restoreMainWindow = new JMenuItem(
            res.getStr("restore.main.window"));
        restoreMainWindow.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_BACK_SPACE,UIKeys.menuKeyMask() |
            KeyEvent.SHIFT_DOWN_MASK));     
        restoreMainWindow.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                MainFrame.get().center();
            }
        });        
        add(restoreMainWindow);        
    }
}
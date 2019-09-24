package com.andcreations.ae.studio.plugins.lua.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaDebugMenu extends JMenu {
    /** */
    private final BundleResources res = new BundleResources(LuaDebugMenu.class);     
    
    /** */
    private LuaDebugMenuListener listener;
    
    /** */
    private JMenuItem continueExecutionItem;
    
    /** */
    private JMenuItem stepIntoItem;
    
    /** */
    private JMenuItem stepOverItem;
    
    /** */
    private JMenuItem stepReturnItem;
    
    /** */
    LuaDebugMenu(LuaDebugMenuListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        setText(res.getStr("menu.text"));
        setMnemonic(KeyEvent.VK_D);    
        
    // step into
        stepIntoItem = new JMenuItem(res.getStr("step.into"),
            Icons.getIcon(LuaDebugIcons.STEP_INTO));
        stepIntoItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));    
        stepIntoItem.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.stepInto();
            }
        });
        add(stepIntoItem);
        
    // step over
        stepOverItem = new JMenuItem(res.getStr("step.over"),
            Icons.getIcon(LuaDebugIcons.STEP_OVER));
        stepOverItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F6,0));    
        stepOverItem.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.stepOver();
            }
        });
        add(stepOverItem);
        
    // step return
        stepReturnItem = new JMenuItem(res.getStr("step.return"),
            Icons.getIcon(LuaDebugIcons.STEP_RETURN));
        stepReturnItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F7,0));    
        stepReturnItem.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.stepReturn();
            }
        });
        add(stepReturnItem);
        
    // continue
        continueExecutionItem = new JMenuItem(res.getStr("continue.execution"),
            Icons.getIcon(LuaDebugIcons.CONTINUE_EXECUTION));
        continueExecutionItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F8,0));    
        continueExecutionItem.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.continueExecution();
            }
        });
        add(continueExecutionItem);
        
    // add to the main menu
        MainFrame.get().getMainFrameMenuBar().add(this);
    }
    
    /** */
    private void setItemsEnabled(boolean enabled) {
        continueExecutionItem.setEnabled(enabled);
        stepIntoItem.setEnabled(enabled);
        stepOverItem.setEnabled(enabled);
        stepReturnItem.setEnabled(enabled);
    }
    
    /** */
    void sessionAttached() {
        setItemsEnabled(true);
    }
    
    /** */
    void sessionDetached() {
        setItemsEnabled(false);
    }
    
    /** */
    void setSuspended(boolean suspended) {
        setItemsEnabled(suspended);
    }
}
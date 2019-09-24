package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.resources.BundleResources;

/**
 * The close action.
 *
 * @author Mikolaj Gucki
 */
public class CloseAction extends SimpleButtonAction {
    /** */
    private final BundleResources res = new BundleResources(CloseAction.class);  
    
    /** */
    private ViewManager viewManager;
    
    /** */
    private View view;
    
    /** */
    CloseAction(ViewManager viewManager,View view) {
        this.viewManager = viewManager;
        this.view = view;
        
        setIcon(Icons.getIcon(DefaultIcons.CLOSE_VIEW));
        setText(res.getStr("action.text"));
        setTooltip(res.getStr("action.tooltip"));
        setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_W,UIKeys.menuKeyMask()));
    }
    
    /** */
    @Override
    public void action(Dockable dockable) {
        viewManager.closeView(view);
    }
}

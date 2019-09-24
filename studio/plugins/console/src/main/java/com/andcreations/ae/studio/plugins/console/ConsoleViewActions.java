package com.andcreations.ae.studio.plugins.console;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButtonListener;
import com.andcreations.resources.BundleResources;

/**
 * Adds the default console actions.
 *
 * @author Mikolaj Gucki
 */
public class ConsoleViewActions {
    /** */
    private static final BundleResources res =
        new BundleResources(ConsoleViewActions.class);    
    
    /** */
    private static void addClearButton(final ConsoleComponent component,
        View view) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_X,UIKeys.menuKeyMask());    
        ViewButton button = view.addButton();
        button.setAccelerator(keyStroke);
        button.setIcon(Icons.getIcon(DefaultIcons.ERASER));
        button.setText(res.getStr("clear"));
        button.setTooltip(res.getStr("clear"));
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                component.clear();
            }
        });
    }
    
    /** */
    private static void addLockButton(final ConsoleComponent component,
        View view) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_L,UIKeys.menuKeyMask());        
        ViewCheckButton button = view.addCheckButton();
        button.setAccelerator(keyStroke);        
        button.setIcon(Icons.getIcon(DefaultIcons.LOCK));
        button.setTooltip(res.getStr("lock"));
        button.addViewCheckButtonListener(new ViewCheckButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewCheckButton button) {
                component.setScrollLock(button.isChecked());
            }
        });    
    }
    
    /** */
    private static void addFindButton(final ConsoleComponent component,
        View view) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_F,UIKeys.menuKeyMask());        
        ViewCheckButton button = view.addCheckButton();
        button.setAccelerator(keyStroke);        
        button.setText(res.getStr("find"));
        button.setTooltip(res.getStr("find"));
        button.setIcon(Icons.getIcon(DefaultIcons.SEARCH));
        button.setAccelerator(keyStroke);        
        button.addViewCheckButtonListener(new ViewCheckButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewCheckButton button) {
                component.setFindPanelVisible(button.isChecked());
            }
        });         
    }
    
    /** */
    public static void addActions(View view) {
        final ConsoleComponent component =
            (ConsoleComponent)view.getComponent();
        
        addClearButton(component,view);
        addLockButton(component,view);
        addFindButton(component,view);
    }
}
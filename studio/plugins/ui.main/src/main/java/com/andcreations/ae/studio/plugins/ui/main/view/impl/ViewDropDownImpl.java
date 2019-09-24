package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.util.ArrayList;
import java.util.List;

import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.actions.SimpleDropDownAction;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDropDown;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj  Gucki
 */
class ViewDropDownImpl implements ViewDropDown,ViewAction {
    /** */
    private final BundleResources res =
        new BundleResources(ViewDropDownImpl.class); 
    
    /** */
    private List<ViewAction> actions = new ArrayList<>();
    
    /** */
    ViewDropDownImpl() {
    }
    
    /** */
    @Override
    public ViewButton addButton() {
        ViewButtonImpl button = new ViewButtonImpl();
        actions.add(button);
        
        return button;
    }
    
    /** */
    @Override
    public DockAction getDockAction() {
        SimpleDropDownAction dropDownAction = new SimpleDropDownAction();
        dropDownAction.setIcon(Icons.getIcon(DefaultIcons.DROPDOWN));
        dropDownAction.setText(res.getStr("action.text"));
        for (ViewAction action:actions) {
            dropDownAction.add(action.getDockAction());
        }
        
        return dropDownAction;
    }
}
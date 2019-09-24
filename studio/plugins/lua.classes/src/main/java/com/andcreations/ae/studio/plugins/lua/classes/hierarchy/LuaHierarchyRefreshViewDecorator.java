package com.andcreations.ae.studio.plugins.lua.classes.hierarchy;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaHierarchyRefreshViewDecorator implements ViewDecorator {
    /** */
    private LuaHierarchyComponent component;
    
    /** */
    LuaHierarchyRefreshViewDecorator(LuaHierarchyComponent component) {
        this.component = component;
    }
    
    /** */
    private BundleResources res =
        new BundleResources(LuaHierarchyRefreshViewDecorator.class);  
        
    /** */
    @Override
    public void decorateView(View view) {
        ViewButton button = view.addButton();
        button.setIcon(Icons.getIcon(DefaultIcons.REFRESH));
        button.setText(res.getStr("text"));
        button.setTooltip(res.getStr("tooltip"));
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                component.refresh();
            }
        });
    }
}
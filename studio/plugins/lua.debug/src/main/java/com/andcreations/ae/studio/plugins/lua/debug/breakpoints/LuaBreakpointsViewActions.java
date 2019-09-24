package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.resources.BundleResources;

/**
 * Adds actions to the breakpoints view.
 *
 * @author Mikolaj Gucki
 */
class LuaBreakpointsViewActions implements ViewDecorator {
    /** */
    private final BundleResources res =
        new BundleResources(LuaBreakpointsViewActions.class);     
    
    /** */
    @Override
    public void decorateView(View view) {
	// enable breakpoints
    	/*
    	ViewCheckButton enable = view.addCheckButton();
    	enable.setIcon(Icons.getIcon(LuaDebugIcons.LUA_BREAKPOINTS));
    	enable.setTooltip(res.getStr("enable.tooltip"));
    	enable.setChecked(true);
    	enable.addViewCheckButtonListener(new ViewCheckButtonListener() {
			@Override
			public void actionPerformed(ViewCheckButton button) {
			}
        });
        */
    	
    // delete
        ViewButton delete = view.addButton();
        delete.setIcon(Icons.getIcon(DefaultIcons.DELETE));
        delete.setTooltip(res.getStr("delete.tooltip"));
        delete.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                LuaBreakpoints.get().deleteSelected();
            }
        });
        
    // delete all
        ViewButton deleteAll = view.addButton();
        deleteAll.setIcon(Icons.getIcon(DefaultIcons.DELETE_ALL));
        deleteAll.setTooltip(res.getStr("delete.all.tooltip"));
        deleteAll.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                if (CommonDialogs.yesNo(res.getStr("delete.all.title"),
                    res.getStr("delete.all.message")) == true) {
                //
                    LuaBreakpoints.get().deleteAll();
                }
            }
        });        
    }
}
package com.andcreations.ae.studio.plugins.search;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikoaj Gucki
 */
class SearchViewActions {
    /** */
    private static final BundleResources res =
        new BundleResources(SearchViewActions.class);    
    
    /** */
    public static void addActions(View view) {
        final SearchViewComponent component =
            (SearchViewComponent)view.getComponent();
     
    // expand all
        ViewButton expandAll = view.addButton();
        expandAll.setIcon(Icons.getIcon(DefaultIcons.EXPAND));
        expandAll.setTooltip(res.getStr("expand.all.tooltip"));
        expandAll.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                Search.get().expandAll();
            }
        });            
     
    // collapse all
        ViewButton collapseAll = view.addButton();
        collapseAll.setIcon(Icons.getIcon(DefaultIcons.COLLAPSE));
        collapseAll.setTooltip(res.getStr("collapse.all.tooltip"));
        collapseAll.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                Search.get().collapseAll();
            }
        });            
            
    // search again
        ViewButton again = view.addButton();
        again.setIcon(Icons.getIcon(DefaultIcons.REFRESH));
        again.setTooltip(res.getStr("search.again.tooltip"));
        again.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                Search.get().searchAgain();
            }
        });            
     
    // clear
        ViewButton clear = view.addButton();
        clear.setIcon(Icons.getIcon(DefaultIcons.ERASER));
        clear.setTooltip(res.getStr("clear.tooltip"));
        clear.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                component.clear();
            }
        });            
    }
}
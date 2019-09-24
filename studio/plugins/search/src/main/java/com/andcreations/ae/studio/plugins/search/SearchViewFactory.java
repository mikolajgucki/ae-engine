package com.andcreations.ae.studio.plugins.search;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;

/**
 * @author Mikolaj Gucki
 */
class SearchViewFactory extends SingleViewFactory {
    /** */
    private boolean actionsAdded;
    
    /** */
    public SearchViewFactory(String viewId,JComponent component,String title,
        ImageIcon icon) {
    //
        super(viewId,component,title,icon);    
    }
    
    /** */
    @Override
    public View createView(String viewId) {
        View view = super.createView(viewId);
        if (actionsAdded == false) {
            SearchViewActions.addActions(view);
            actionsAdded = true;
        }
        return view;
    }
}
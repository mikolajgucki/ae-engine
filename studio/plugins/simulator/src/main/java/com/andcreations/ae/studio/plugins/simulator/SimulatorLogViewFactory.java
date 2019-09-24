package com.andcreations.ae.studio.plugins.simulator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.andcreations.ae.studio.plugins.console.ConsoleViewActions;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;

/**
 * @author Mikolaj Gucki
 */
class SimulatorLogViewFactory extends SingleViewFactory {    
    /**
     * Constructs a {@link SimulatorLogViewFactory}.
     *
     * @param viewId The view identifier.
     * @param component The view component.
     * @param title The view title.
     * @param icon The view icon.
     */
    public SimulatorLogViewFactory(String viewId,JComponent component,
        String title,ImageIcon icon) {
    //
        super(viewId,component,title,icon);
    }    
    
    /** */
    @Override
    public View createView(String viewId) {
        View view = super.createView(viewId);
        ConsoleViewActions.addActions(view);
        
        return view;
    }
}
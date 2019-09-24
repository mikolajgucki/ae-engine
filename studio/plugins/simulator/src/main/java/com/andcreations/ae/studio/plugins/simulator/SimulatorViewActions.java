package com.andcreations.ae.studio.plugins.simulator;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorViewActions {
    /** */
    private static final BundleResources res =
        new BundleResources(SimulatorViewActions.class);    

    /** */
    static void addActions(View view) {
    // delete state
        ViewButton deleteState = view.addButton();
        deleteState.setIcon(Icons.getIcon(DefaultIcons.DELETE));
        deleteState.setTooltip(res.getStr("delete.state.tooltip"));
        deleteState.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                SimulatorStateUtil.deleteState();
            }
        });           
    }
}
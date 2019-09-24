package com.andcreations.ae.studio.plugins.ui.main.finalizer;

import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;

/**
 * @author Mikolaj Gucki
 */
public class UIMainFinalizer {
    /** */
    static void showMainFrame(final UIMainFinalizerState state) {
    	// The main frame is shown in a finalizer as there might views provided
    	// by project plugins. Project plugins are started after all the core
    	// plugins have been started.
    	PluginManager.addPluginFinalizer(new Runnable() {
            /** */
            @Override
            public void run() {
		        UICommon.invokeAndWait(new Runnable() {
		            /** */
		            public void run() {
		                MainFrame.get().setMainFrameState(
	                		state.getMainFrameState());
		                MainFrame.get().getViewManager().restoreViews();
		                MainFrame.get().showMainFrame();
		                MainFrame.get().getViewManager().restoreViewLayout();
		            }
		        });
            }
    	});
    }
}

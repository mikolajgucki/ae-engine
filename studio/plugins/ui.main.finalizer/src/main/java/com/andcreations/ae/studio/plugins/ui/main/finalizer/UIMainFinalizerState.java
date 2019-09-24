package com.andcreations.ae.studio.plugins.ui.main.finalizer;

import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugins.ui.main.MainFrameState;

/**
 * @author Mikolaj Gucki
 */
public class UIMainFinalizerState extends PluginState {
    /** */
    private MainFrameState mainFrameState;

    /** */
    public MainFrameState getMainFrameState() {
        if (mainFrameState == null) {
            mainFrameState = new MainFrameState();
        }
        
        return mainFrameState;
    }

    /** */
    public void setMainFrameState(MainFrameState mainFrameState) {
        this.mainFrameState = mainFrameState;
    }
}

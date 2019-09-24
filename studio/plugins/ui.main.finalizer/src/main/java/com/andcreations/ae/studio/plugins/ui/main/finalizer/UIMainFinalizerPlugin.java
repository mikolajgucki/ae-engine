package com.andcreations.ae.studio.plugins.ui.main.finalizer;

import com.andcreations.ae.studio.plugin.Finalizer;
import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;

/**
 * @author Mikolaj Gucki
 */
@Finalizer
@StartAfterAndRequire(id="com.andcreations.ae.studio.plugins.ui.main")
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.ui.common"
})
public class UIMainFinalizerPlugin extends Plugin<UIMainFinalizerState> {
    /** */
    private UIMainFinalizerState state;
    
	/** */
	@Override
	public void start() {
		UIMainFinalizer.showMainFrame(getState());
	}
	
	/** */
	@Override
	public void stop() {
	    state.setMainFrameState(MainFrame.get().getMainFrameState());
	}
	
    /** */
    @Override
    public UIMainFinalizerState getState() {
        return state;
    }
    
    /** */
    @Override
    public void setState(UIMainFinalizerState state) {
        if (state == null) {
            state = new UIMainFinalizerState();
        }
        this.state = state;
    }	
}
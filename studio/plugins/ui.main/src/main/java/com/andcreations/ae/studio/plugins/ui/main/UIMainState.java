package com.andcreations.ae.studio.plugins.ui.main;

import com.andcreations.ae.studio.plugin.PluginState;

/**
 * State the of UIMain plugin.
 * 
 * @author Mikolaj Gucki
 */
public class UIMainState extends PluginState {
	/** The main frame state. */
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

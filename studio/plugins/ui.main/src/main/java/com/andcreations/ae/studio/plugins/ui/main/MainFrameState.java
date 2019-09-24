package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.Rectangle;

/**
 * The main frame state.
 * 
 * @author Mikolaj Gucki
 */
public class MainFrameState {
	/** The frame bounds. */
	private Rectangle bounds;
	
	/** */
	private boolean maximimzed;
	
	/** */
	private String viewManagerState;
	
	/** */
	Rectangle getBounds() {		
		return bounds;
	}
	
	/** */
	void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	/** */
	boolean isMaximized() {
		return maximimzed;
	}
	
	/** */
	void setMaximized(boolean maximimzed) {
		this.maximimzed = maximimzed;
	}
	
	/** */
	void setViewManagerState(String viewManagerState) {
		this.viewManagerState = viewManagerState;
	}
	
	/** */
	String getViewManagerState() {
		return viewManagerState;
	}
}

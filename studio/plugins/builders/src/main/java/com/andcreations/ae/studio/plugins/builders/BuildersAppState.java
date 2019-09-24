package com.andcreations.ae.studio.plugins.builders;

/**
 * @author Mikolaj Gucki
 */
public class BuildersAppState {
	/** */
	private boolean showConsoleWhenBuilderStarts = true;
	
	/** */
	private boolean clearConsoleWhenBuilderStarts = true;

	/** */
	public boolean getShowConsoleWhenBuilderStarts() {
		return showConsoleWhenBuilderStarts;
	}

	/** */
	public void setShowConsoleWhenBuilderStarts(
		boolean showConsoleWhenBuilderStarts) {
	//
		this.showConsoleWhenBuilderStarts = showConsoleWhenBuilderStarts;
	}

	/** */
	public boolean getClearConsoleWhenBuilderStarts() {
		return clearConsoleWhenBuilderStarts;
	}

	/** */
	public void setClearConsoleWhenBuilderStarts(
		boolean clearConsoleWhenBuilderStarts) {
	//
		this.clearConsoleWhenBuilderStarts = clearConsoleWhenBuilderStarts;
	}
}

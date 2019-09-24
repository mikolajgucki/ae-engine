package com.andcreations.ae.studio.plugins.builders;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.preferences.Preferences;

/**
 * @author Mikolaj Gucki
 */
public class BuildersPreferences {
	/** */
	private static BuildersPreferences instance;
	
	/** */
	private BuildersAppState appState;
	
	/** */
	void init(final BuildersAppState appState) {
		this.appState = appState;
		
	// page
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                Preferences.get().addPage(
            		new BuildersPreferencesPage(appState));
            }
        });		
	}
	
	/** */
	public boolean getClearConsoleWhenBuilderStarts() {
		return appState.getClearConsoleWhenBuilderStarts();
	}
	
	/** */
	public boolean getShowConsoleWhenBuilderStarts() {
		return appState.getShowConsoleWhenBuilderStarts();
	}
	
	/** */
	public static BuildersPreferences get() {
		if (instance == null) {
			instance = new BuildersPreferences();
		}
		
		return instance;
	}
}

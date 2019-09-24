package com.andcreations.ae.studio.plugins.builders;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.main.preferences.PreferencesPage;
import com.andcreations.ae.studio.plugins.builders.resources.R;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class BuildersPreferencesPage extends PreferencesPage {
    /** */
    private BundleResources res =
        new BundleResources(BuildersPreferencesPage.class);	
    
    /** */
    private BuildersAppState appState;
    
    /** */
    private JCheckBox showConsoleCheckBox;
    
    /** */
    private JCheckBox clearConsoleCheckBox;
	
	/** */
	BuildersPreferencesPage(BuildersAppState appState) {
		this.appState = appState;
		create();
	}
	
	/** */
	private void create() {
		setTitle(res.getStr("title"));
		setIconName(BuildersIcons.BUILDER);
		
	// create
		createComponents();
		
	// layout
        JPanel panel = layoutComponents();
        setComponent(panel);  		
	}
	
	/** */
	private void createComponents() {
	// show console
		showConsoleCheckBox = new JCheckBox(res.getStr("show.console"));
		
	// clear console
		clearConsoleCheckBox = new JCheckBox(res.getStr("clear.console"));
	}
	
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(R.class,
            "BuildersPreferencesPage.formlayout");
        
    // add components
        layout.addComponent("sc",showConsoleCheckBox);
        layout.addComponent("cc",clearConsoleCheckBox);
        
    // layout
        return layout.build();  
    }
	
	/** */
	@Override
	public void update() {
	// console
		showConsoleCheckBox.setSelected(
			appState.getShowConsoleWhenBuilderStarts());
		clearConsoleCheckBox.setSelected(
			appState.getClearConsoleWhenBuilderStarts());
	}

	/** */
	@Override
	public void apply() {
	// console
		appState.setShowConsoleWhenBuilderStarts(
			showConsoleCheckBox.isSelected());
		appState.setClearConsoleWhenBuilderStarts(
			clearConsoleCheckBox.isSelected());
	}
}

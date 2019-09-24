package com.andcreations.ae.studio.plugins.simulator;

import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.simulator.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.main.preferences.PreferencesPage;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
class SimulatorPreferencesPage extends PreferencesPage {
    /** */
    private BundleResources res =
        new BundleResources(SimulatorPreferencesPage.class);
    
    /** */  
    private SimulatorState state;
    
    /** */
    private JCheckBox clearSimulatorLogCheck;
    
    /** */
    private JCheckBox showSimulatorLogCheck;
        
    /** */
    SimulatorPreferencesPage(SimulatorState state) {
        this.state = state;
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        setIconName(SimulatorIcons.SIMULATOR);
        
    // create components
        createComponents();
        
    // layout components
        JPanel panel = layoutComponents();
        setComponent(panel);
    }
    
    /** */
    private void createComponents() {
    // show simulator log
        showSimulatorLogCheck = new JCheckBox(
            res.getStr("show.simulator.log"));
        
    // clear simulator log
        clearSimulatorLogCheck = new JCheckBox(
            res.getStr("clear.simulator.log"));
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"SimulatorPreferencesPage.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout " + exception.getMessage());
            return null;
        }
        
        builder.add(showSimulatorLogCheck,"s");
        builder.add(clearSimulatorLogCheck,"c");
        
        return builder.getPanel();
    }    
    
    /** */
    @Override
    public void update() {
        showSimulatorLogCheck.setSelected(state.getShowLog());
        clearSimulatorLogCheck.setSelected(state.getClearLog());
    }
    
    /** */
    @Override
    public void apply() {
        state.setShowLog(showSimulatorLogCheck.isSelected());
        state.setClearLog(clearSimulatorLogCheck.isSelected());
    }
}
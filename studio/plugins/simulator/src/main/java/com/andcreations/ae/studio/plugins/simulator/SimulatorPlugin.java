package com.andcreations.ae.studio.plugins.simulator;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.ae.dist.AEDistPlugin;
import com.andcreations.ae.studio.plugins.project.ProjectPlugin;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.preferences.Preferences;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.lua.lib",    
    "com.andcreations.ae.studio.plugins.lua.debug",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.lua.compiler",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.text.editor"
})
public class SimulatorPlugin extends Plugin<SimulatorState> {
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.ae.dist")
    private Object aeDistPluginObject;
    
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.project")
    private Object projectPluginObject;
    
    /** */
    private SimulatorState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        SimulatorIcons.init();
        createLog();
        createSimulator();
        createPreferences();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        Simulator.get().stop();
    }
    
    /** */
    @Override
    public SimulatorState getState() {
        return state;
    }
    
    /** */
    @Override
    public void setState(SimulatorState state) {
        if (state == null) {
            state = new SimulatorState();
            state.initWithDefaults();
        }
        this.state = state;
    }    
    
    /** */
    private void createLog() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                SimulatorLog.initUI();
            }
        });        
    }
    
    /** */
    private void createSimulator() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                Simulator.get().init(getState(),getStorageDir(),
                    (AEDistPlugin)aeDistPluginObject,
                    (ProjectPlugin)projectPluginObject);
            }
        });
    }
    
    /** */
    private void createPreferences() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                SimulatorPreferencesPage page =
                    new SimulatorPreferencesPage(getState());
                Preferences.get().addPage(page);
            }
        });           
    }
}
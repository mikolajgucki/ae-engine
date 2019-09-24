package com.andcreations.ae.studio.plugins.problems;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.StartAfter;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * The plugins which displays problems (error, warnings) in a table.
 * 
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main"
})
@StartAfter(id={  
    // Start after selecting the project to store the Problems plugin state
    // in the project directory.
    "com.andcreations.ae.studio.plugins.project.select"
})
public class ProblemsPlugin extends Plugin<ProblemsState> {
    /** The state. */
    private ProblemsState state;
    
    /** */
    @Override
    public void start() throws PluginException {
        Problems.get().initUI();
    }
    
    /** */
    @Override
    public ProblemsState getState() {
        if (state == null) {
            state = new ProblemsState();
        }
        
        return state;
    }
    
    /** */
    @Override
    public void setState(ProblemsState state) {
        this.state = state;
    }
}
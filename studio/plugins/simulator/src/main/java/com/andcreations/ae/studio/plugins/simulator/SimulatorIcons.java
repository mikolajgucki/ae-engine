package com.andcreations.ae.studio.plugins.simulator;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class SimulatorIcons {
    public static final String SIMULATOR = "simulator";
    public static final String DECO_SIMULATOR = "deco_simulator";
        
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(SimulatorIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(SIMULATOR,Icons.orange());
        Icons.colorize(DECO_SIMULATOR,Icons.orange());
    }
}
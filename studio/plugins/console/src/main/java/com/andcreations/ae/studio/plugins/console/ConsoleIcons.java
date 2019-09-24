package com.andcreations.ae.studio.plugins.console;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class ConsoleIcons {
    /** */
    public static final String CONSOLE = "console";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(ConsoleIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(CONSOLE,Icons.green());
    }
}
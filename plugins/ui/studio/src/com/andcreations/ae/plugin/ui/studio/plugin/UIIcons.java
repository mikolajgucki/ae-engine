package com.andcreations.ae.plugin.ui.studio.plugin;

import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;
import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;

/**
 * @author Mikolaj Gucki
 */
public class UIIcons {
    /** */
    public static final String LAYOUT = "layout";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(UIIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(LAYOUT,Icons.green());
    }
}
package com.andcreations.ae.studio.plugins.outline;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
class OutlineIcons {
    public static final String OUTLINE = "outline";
    
    /** */
    static void init() {
    // icons
        IconSource iconSource =
            new ResourceIconSource(OutlineIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(OUTLINE,Icons.orange());
    }
}
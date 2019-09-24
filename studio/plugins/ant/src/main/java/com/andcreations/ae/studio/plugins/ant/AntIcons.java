package com.andcreations.ae.studio.plugins.ant;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class AntIcons {
    /** */
    public static final String ANT = "ant";
        
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(AntIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(ANT,Icons.orange());
    }
}
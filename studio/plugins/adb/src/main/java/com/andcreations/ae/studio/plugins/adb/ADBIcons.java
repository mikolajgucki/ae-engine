package com.andcreations.ae.studio.plugins.adb;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class ADBIcons {
    /** */
    public static final String LOG_CAT = "log_cat";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(ADBIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(LOG_CAT,Icons.green());
    }
}
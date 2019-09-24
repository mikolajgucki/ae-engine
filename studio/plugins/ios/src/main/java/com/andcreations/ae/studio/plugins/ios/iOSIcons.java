package com.andcreations.ae.studio.plugins.ios;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class iOSIcons {
    /** */
    public static final String IOS = "ios";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(iOSIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(IOS,Icons.lightGray());
    }
}
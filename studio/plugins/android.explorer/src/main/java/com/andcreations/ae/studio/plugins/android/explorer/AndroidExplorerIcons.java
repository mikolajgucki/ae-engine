package com.andcreations.ae.studio.plugins.android.explorer;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class AndroidExplorerIcons {
    /** */
    public static final String ANDROID_EXPLORER = "android_explorer";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(AndroidExplorerIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(ANDROID_EXPLORER,Icons.green());
    }
}
package com.andcreations.ae.studio.plugins.assets;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class AssetsIcons {
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(AssetsIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
    }
}
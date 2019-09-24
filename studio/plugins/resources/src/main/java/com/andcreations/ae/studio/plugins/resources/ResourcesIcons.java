package com.andcreations.ae.studio.plugins.resources;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class ResourcesIcons {
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(ResourcesIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
    }
}
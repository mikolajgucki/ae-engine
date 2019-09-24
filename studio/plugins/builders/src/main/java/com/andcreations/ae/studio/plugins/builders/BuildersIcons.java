package com.andcreations.ae.studio.plugins.builders;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class BuildersIcons {
    /** */
    public static final String BUILDER = "builder";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(BuildersIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(BUILDER,Icons.lightGray());
    }
}
package com.andcreations.ae.studio.plugins.assets.fonts;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class FontsIcons {
    /** */
    public static final String FONT = "font";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(FontsIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(FONT,Icons.violet());
    }
}
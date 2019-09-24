package com.andcreations.ae.studio.plugins.assets.textures;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class TexturesIcons {
    /** */
    public static final String TEXTURE = "texture";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(TexturesIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(TEXTURE,Icons.violet());
    }
}
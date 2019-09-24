package com.andcreations.ae.studio.plugins.assets.fonts;

import java.io.File;

import com.andcreations.ae.assets.TexFontBuilder;
import com.andcreations.ae.studio.plugins.assets.AbstractAssetsBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
class SingleTexFontBuilder extends AbstractAssetsBuilder {
    /** */
    public static final String ID = "font";
    
    /** */
    private TexFontBuilder texFontBuilder;
    
    /** */
    private File file;
    
    /** */
    SingleTexFontBuilder(TexFontBuilder texFontBuilder,File file) {
        super(ID,file.getName(),Icons.getIcon(FontsIcons.FONT),"");
        this.texFontBuilder = texFontBuilder;
        this.file = file;
    }
    
    /** */
    @Override
    public void build() {
        texFontBuilder.buildTexFontFile(file);
    }    
}
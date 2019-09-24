package com.andcreations.ae.studio.plugins.assets.textures;

import java.io.File;

import com.andcreations.ae.assets.TexPackBuilder;
import com.andcreations.ae.studio.plugins.assets.AbstractAssetsBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
class SingleTexPackBuilder extends AbstractAssetsBuilder {
    /** */
    public static final String ID = "texture";
    
    /** */
    private TexPackBuilder texPackBuilder;
    
    /** */
    private File file;
    
    /** */
    SingleTexPackBuilder(TexPackBuilder texPackBuilder,File file) {
        super(ID,file.getName(),Icons.getIcon(TexturesIcons.TEXTURE),"");
        this.texPackBuilder = texPackBuilder;
        this.file = file;
    }
    
    /** */
    @Override
    public void build() {
        texPackBuilder.buildTexPackFile(file);
    }    
}
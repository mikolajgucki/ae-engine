package com.andcreations.ae.studio.plugins.assets;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.builders.AbstractBuilder;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractAssetsBuilder extends AbstractBuilder
    implements AssetsBuilder {
    /** */
    protected AbstractAssetsBuilder(String id,String name,ImageIcon icon,
        String desc) {
    //
        super(id,name,icon,desc);
    }
}
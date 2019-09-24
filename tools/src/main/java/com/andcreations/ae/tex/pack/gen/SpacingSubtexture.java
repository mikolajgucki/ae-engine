package com.andcreations.ae.tex.pack.gen;

import com.andcreations.ae.tex.pack.data.TexSpacing;
import com.andcreations.ae.tex.pack.strategy.AbstractSubtexture;

/**
 * @author Mikolaj Gucki
 */
class SpacingSubtexture extends AbstractSubtexture {
    /** */
    private AbstractSubtexture subtexture;
    
    /** */
    private TexSpacing spacing;
    
    /** */
    SpacingSubtexture(String id,AbstractSubtexture subtexture,
        TexSpacing spacing) {
    //
        super(id);
        this.subtexture = subtexture;
        this.spacing = spacing;
        create();
    }
    
    /** */
    private void create() {
        if (spacing == null) {
            spacing = new TexSpacing(0);
        }
    }
    
    /** */
    @Override
    public int getWidth() {
        return subtexture.getWidth() + spacing.getLeft() + spacing.getRight();
    }
    
    /** */
    @Override
    public int getHeight() {
        return subtexture.getHeight() + spacing.getTop() + spacing.getBottom();
    }
    
    /** */
    @Override
    public void setLocation(int x,int y) {
        subtexture.setLocation(x + spacing.getLeft(),y + spacing.getTop());
    }
}
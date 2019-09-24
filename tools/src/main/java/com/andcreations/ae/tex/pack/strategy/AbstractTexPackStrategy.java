package com.andcreations.ae.tex.pack.strategy;

import java.util.List;

import com.andcreations.ae.tex.pack.TexPackException;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractTexPackStrategy implements TexPackStrategy {
    /** The texture width. */
    private int width;
    
    /** The texture height. */
    private int height;
    
    /**
     * Gets the texture width.
     *
     * @return The texture width.
     */
    protected int getWidth() {
        return width;
    }
    
    /**
     * Gets the texture height.
     *
     * @return The texture height.
     */
    protected int getHeight() {
        return height;
    }
    
    /** */
    @Override
    public void pack(int width,int height,List<Subtexture> subtextures)
        throws TexPackException {
    //
        this.width = width;
        this.height = height;
        
        pack(subtextures);
    }
    
    /** */
    public abstract void pack(List<Subtexture> subtextures)
        throws TexPackException,SubtexturesDontFitException;
}
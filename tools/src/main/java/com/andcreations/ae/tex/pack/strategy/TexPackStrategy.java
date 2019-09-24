package com.andcreations.ae.tex.pack.strategy;

import java.util.List;

import com.andcreations.ae.tex.pack.TexPackException;

/**
 * The interface for texture packer algorithms.
 *
 * @author Mikolaj Gucki
 */
public interface TexPackStrategy {
    /**
     * Packs a list of images.
     *
     * @param width The texture width.
     * @param height The texture height.
     * @param subtextures The subtextures to pack.
     * @throws TexPackException if texture packing fails.
     * @throws SubtexturesDontFitException if the subtextures don't fit.
     */
    void pack(int width,int height,List<Subtexture> subtextures)
        throws TexPackException,SubtexturesDontFitException;
}
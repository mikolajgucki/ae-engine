package com.andcreations.ae.tex.pack.strategy;

/**
 * The interface for images packed into a texture.
 *
 * @author Mikolaj Gucki
 */
public interface Subtexture {
    /**
     * Gets the image identifier.
     *
     * @return The image identifier.
     */
    String getId();
    
    /**
     * Gets the image width.
     *
     * @return The image width.
     */
    int getWidth();
    
    /**
     * Gets the image height.
     *
     * @return The image height.
     */
    int getHeight();
    
    /**
     * Sets the image location in the texture.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    void setLocation(int x,int y);
}
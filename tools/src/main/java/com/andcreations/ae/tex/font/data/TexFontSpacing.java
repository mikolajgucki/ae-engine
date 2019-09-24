package com.andcreations.ae.tex.font.data;

/**
 * Provides horizontal and vertical space to put between characters in
 * the output image.
 *
 * @author Mikolaj Gucki
 */
public class TexFontSpacing {
    /** The default horizontal and vertical spacing. */
    private static final int DEFAULT_SPACING = 8;
    
    /** The horizontal spacing. */
    private int horizontal;
    
    /** The vertical spacing. */
    private int vertical;
    
    /**
     * Constructs a {@link TexFontSpacing} using the default values.
     */
    public TexFontSpacing() {
        this(DEFAULT_SPACING,DEFAULT_SPACING);
    }
    
    /**
     * Constructs a {@link TexFontSpacing} using the default values.
     *
     * @param horizontal The horizontal spacing.
     * @param vertical The vertical spacing.
     */
    public TexFontSpacing(int horizontal,int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
    
    /**
     * Gets the horizontal spacing.
     *
     * @return The horizontal spacing.
     */
    public int getHorizontal() {
        return horizontal;
    }
    
    /**
     * Gets the vertical spacing.
     *
     * @return The vertical spacing.
     */
    public int getVertical() {
        return vertical;
    }
}
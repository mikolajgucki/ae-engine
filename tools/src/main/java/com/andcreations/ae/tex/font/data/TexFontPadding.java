package com.andcreations.ae.tex.font.data;

/**
 * @author Mikolaj Gucki
 */
public class TexFontPadding {
    /** Indicates no padding. */
    public static final int NO_PADDING = 0;
    
    /** The top padding. */
    private int top = NO_PADDING;
    
    /** The bottom padding. */
    private int bottom = NO_PADDING;
    
    /** The left padding. */
    private int left = NO_PADDING;
    
    /** The right padding. */
    private int right = NO_PADDING;
    
    /**
     * Gets the top padding.
     *
     * @return The top padding.
     */
    public int getTop() {
        return top;
    }
    
    /**
     * Gets the bottom padding.
     *
     * @return The bottom padding.
     */
    public int getBottom() {
        return bottom;
    }
    
    /**
     * Gets the left padding.
     *
     * @return The left padding.
     */
    public int getLeft() {
        return left;
    }
    
    /**
     * Gets the right padding.
     *
     * @return The right padding.
     */
    public int getRight() {
        return right;
    }    
}
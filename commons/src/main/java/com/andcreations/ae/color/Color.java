package com.andcreations.ae.color;

/**
 * RGBA color.
 *
 * @author Mikolaj Gucki
 */
public class Color {
    /** The red component. */
    public int r;

    /** The green component. */
    public int g;

    /** The blue component. */
    public int b;

    /** The alpha component. */
    public int a;
    
    /** */
    public Color() {
    }
    
    /** */
    public Color(int r,int g,int b,int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    /** */
    public Color(int r,int g,int b) {
        this(r,g,b,255);
    }
    
    /** */
    public Color(int rgba) {
        this.r = (rgba >> 16) & 0xff;
        this.g = (rgba >> 8) & 0xff;
        this.b = rgba & 0xff;
        this.a = (rgba >> 24) & 0xff;
    }
    
    /** */
    public Color(Color color) {
        this(color.r,color.g,color.b,color.a);
    }
    
    /** */
    public int getRed() {
        return r;
    }
    
    /** */
    public int getGreen() {
        return g;
    }
    
    /** */
    public int getBlue() {
        return b;
    }
    
    /** */
    public int getAlpha() {
        return a;
    }
    
    /**
     * Multiplies the components of this color by components of another color.
     *
     * @param color The color.
     */
    public void mul(Color color) {
        r = r * color.r / 255;
        g = g * color.g / 255;
        b = b * color.b / 255;
        a = a * color.a / 255;
    }
    
    /**
     * Gets the average gray color.
     *
     * @return The average gray color.
     */
    public Color avg() {
        int v = (r + g + b) / 3;
        return new Color(v,v,v);
    }
    
    /**
     * Gets the corresponding AWT color.
     * 
     * @return The AWT color.
     */
    public java.awt.Color getAWTColor() {
        return new java.awt.Color(r,g,b,a);
    }
    
    /** */
    public int toRGBA() {
        return a << 24 | r << 16 | g << 8 | b;  
    }
    
    /** */
    public static Color blend(Color a,Color b,double t) {
        return new Color(
            (int)(a.r * t + b.r * (1 - t)),
            (int)(a.g * t + b.g * (1 - t)),
            (int)(a.b * t + b.b * (1 - t)));    
    }
    
    /**
     * Converts a color from a hex.
     *
     * @param hex The hex.
     * @return The color.
     */
    public static Color fromHex(String hex) {
        if (hex.length() != 6 && hex.length() != 8) {
            throw new IllegalArgumentException("Invalid hex value");
        }
        
        Color color = new Color();
        color.r = Integer.parseInt(hex.substring(0,2),16);
        color.g = Integer.parseInt(hex.substring(2,4),16);
        color.b = Integer.parseInt(hex.substring(4,6),16);
        
        if (hex.length() == 8) {
            color.a = Integer.parseInt(hex.substring(6,8),16);
        }
        else {
            color.a = 255;
        }
        
        return color;
    }
}
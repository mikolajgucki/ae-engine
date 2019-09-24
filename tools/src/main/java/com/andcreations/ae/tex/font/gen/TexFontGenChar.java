package com.andcreations.ae.tex.font.gen;

/**
 * @author Mikolaj Gucki
 */
public class TexFontGenChar {
    /** */
    private int ch;
    
    /** */
    private String subtexture;
    
    /** */
    private int x;
    
    /** */
    private int y;
    
    /** */
    private int width;
    
    /** */
    private int height;
    
    /** */
    TexFontGenChar() {
    }
    
    /** */
    TexFontGenChar(int ch,String subtexture,int x,int y,int width,int height) {
        this.ch = ch;
        this.subtexture = subtexture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /** */
    public int getCh() {
        return ch;
    }
    
    /** */
    public String getSubtexture() {
        return subtexture;
    }
    
    /** */
    public int getX() {
        return x;
    }
    
    /** */
    public int getY() {
        return y;
    }
    
    /** */
    public int getWidth() {
        return width;
    }
    
    /** */
    public int getHeight() {
        return height;
    }
}
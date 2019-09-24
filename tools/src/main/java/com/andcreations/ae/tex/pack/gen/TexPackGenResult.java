package com.andcreations.ae.tex.pack.gen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexPackGenResult {
    /** */
    private int width;
    
    /** */
    private int height;
    
    /** */
    private List<TexPackGenSubtexture> subtextures = new ArrayList<>();
    
    /** */
    TexPackGenResult(int width,int height) {
        this.width = width;
        this.height = height;
    }
    
    /** */
    public int getWidth() {
        return width;
    }
    
    /** */
    public int getHeight() {
        return height;
    }
    
    /** */
    public double getPixelWidth() {
        return 1d / width;
    }
    
    /** */
    public double getPixelHeight() {
        return 1d / height;
    }
    
    /** */
    void addSubtexture(TexPackGenSubtexture subtexture) {
        subtextures.add(subtexture);
    }
    
    /** */
    public List<TexPackGenSubtexture> getSubtextures() {
        return subtextures;
    }
}
package com.andcreations.ae.tex.pack.gen;

/**
 * @author Mikolaj Gucki
 */
public class TexPackGenUVCoords {
    /** */
    private double u;
    
    /** */
    private double v;
    
    /** */
    TexPackGenUVCoords(double u,double v) {
        this.u = u;
        this.v = v;
    }
    
    /** */
    public double getU() {
        return u;
    }
    
    public double getV() {
        return v;
    }
}
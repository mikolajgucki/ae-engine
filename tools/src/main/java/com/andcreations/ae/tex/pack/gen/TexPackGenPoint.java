package com.andcreations.ae.tex.pack.gen;


/**
 * @author Mikolaj Gucki
 */
public class TexPackGenPoint {
    /** */
    private String id;
    
    /** */
    private TexPackGenUVCoords coords;
    
    /** */
    TexPackGenPoint(String id,TexPackGenUVCoords coords) {
        this.id = id;
        this.coords = coords;
    }
    
    /** */
    public String getId() {
        return id;
    }
    
    /** */
    public TexPackGenUVCoords getCoords() {
        return coords;
    }        
}
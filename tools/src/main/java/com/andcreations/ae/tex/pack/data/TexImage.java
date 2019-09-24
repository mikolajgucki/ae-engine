package com.andcreations.ae.tex.pack.data;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexImage {
    /** */
    private String id;
    
    /** */
    private TexInset inset;
    
    /** */
    private List<TexPoint> points;
    
    /** */
    public void setId(String id) {
        this.id = id;
    }
    
    /** */
    public String getId() {
        return id;
    }    
        
    /** */
    public void setInset(TexInset inset) {
        this.inset = inset;
    }
    
    /** */
    public TexInset getInset() {
        return inset;
    }    
    
    /** */
    public void setPoint(List<TexPoint> points) {
        this.points = points;
    }
    
    /** */
    public List<TexPoint> getPoints() {
        return points;
    }
}
package com.andcreations.ae.tex.pack.data;

/**
 * @author Mikolaj Gucki
 */
public class TexPoint {
    /** */
    private String id;
    
    /** */
    private double x;
    
    /** */
    private double y;
    
    /** */
    public TexPoint() {
    }
    
    /** */
    public TexPoint(String id,double x,double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    /** */
    public String getId() {
        return id;
    }
    
    /** */
    public void setId(String id) {
        this.id = id;
    }
    
    /** */
    public double getX() {
        return x;
    }
    
    /** */
    public void setX(double x) {
        this.x = x;
    }
    
    /** */
    public double getY() {
        return y;
    }
    
    /** */
    public void setY(double y) {
        this.y = y;
    }
}
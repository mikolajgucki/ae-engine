package com.andcreations.ae.tex.pack.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexPackGenSubtexture {
    /** */
    private String id;
    
    /** */
    private TexPackGenUVCoords coords;
    
    /** */
    private double width;
    
    /** */
    private double height;
    
    /** */
    private double aspect;
    
    /** */
    private List<TexPackGenPoint> points;
    
    /** */
    TexPackGenSubtexture(String id,TexPackGenUVCoords coords,double width,
        double height,double aspect) {
    //
        this.id = id;
        this.coords = coords;
        this.width = width;
        this.height = height;
        this.aspect = aspect;
    }
    
    /** */
    public String getId() {
        return id;
    }
    
    /** */
    public TexPackGenUVCoords getCoords() {
        return coords;
    }
    
    /** */
    public double getWidth() {
        return width;
    }
    
    /** */
    public double getHeight() {
        return height;
    }
    
    /** */
    public double getAspect() {
        return aspect;
    }
    
    /** */
    void addPoint(TexPackGenPoint point) {
        if (points == null) {
            points = new ArrayList<>();
        }
        points.add(point);
    }
    
    /** */
    public List<TexPackGenPoint> getPoints() {
        if (points == null) {
            return null;
        }
        return Collections.unmodifiableList(points);
    }
}
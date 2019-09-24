package com.andcreations.ae.tex.pack.strategy;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractSubtexture implements Subtexture {
    /** */
    private String id;
    
    /** */
    private int x;
    
    /** */
    private int y;
    
    /** */
    protected AbstractSubtexture(String id) {
        this.id = id;
    }
        
    /** */
    @Override
    public String getId() {
        return id;
    }
    
    /** */
    @Override
    public abstract int getWidth();
    
    /** */
    @Override
    public abstract int getHeight();
    
    /** */
    @Override
    public void setLocation(int x,int y) {
        this.x = x;
        this.y = y;
    }
    
    /** */
    public int getX() {
        return x;
    }
    
    /** */
    public int getY() {
        return y;
    }
}
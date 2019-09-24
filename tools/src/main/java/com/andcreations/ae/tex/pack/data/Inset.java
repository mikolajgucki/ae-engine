package com.andcreations.ae.tex.pack.data;

/**
 * @author Mikolaj Gucki
 */
class Inset<T> {
    /** */
    private T none;
    
    /** */
    private T size;
    
    /** */
    private T top;
    
    /** */
    private T right;
    
    /** */
    private T bottom;
    
    /** */
    private T left;
    
    /** */
    protected Inset(T none) {
        this.none = none;
        this.size = none;
        this.top = none;
        this.right = none;
        this.bottom = none;
        this.left = none;
    }
    
    /** */
    public void setSize(T size) {
        this.size = size;
    }
    
    /** */
    public T getSize() {
        return size;
    }
    
    /** */
    public T getTop() {
        if (top == none) {
            return size;
        }        
        return top;
    }
    
    /** */
    public T getBottom() {
        if (bottom == none) {
            return size;
        }
        return bottom;
    }
    
    /** */
    public T getLeft() {
        if (left == none) {
            return size;
        }
        return left;
    }
    
    /** */     
    public T getRight() {
        if (right == none) {
            return size;
        }        
        return right;
    }
}
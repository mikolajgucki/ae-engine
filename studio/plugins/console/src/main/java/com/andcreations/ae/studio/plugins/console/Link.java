package com.andcreations.ae.studio.plugins.console;

/**
 * @author Mikolaj Gucki
 */
class Link {
    /** */
    private int offset;
    
    /** */
    private int length;
    
    /** */
    private LinkListener listener;
    
    /** */
    Link(int offset,int length,LinkListener listener) {
        this.offset = offset;
        this.length = length;
        this.listener = listener;
    }
    
    /** */
    boolean inside(int position) {
        return position >= offset && position < offset + length;
    }
    
    /** */
    LinkListener getLinkListener() {
        return listener;
    }
}
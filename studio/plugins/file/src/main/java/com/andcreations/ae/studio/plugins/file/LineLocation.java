package com.andcreations.ae.studio.plugins.file;

import com.andcreations.resources.BundleResources;

/**
 * Represents a line location in a resource (file).
 *
 * @author Mikolaj Gucki
 */
public class LineLocation {
    /** */
    private final BundleResources res =
        new BundleResources(LineLocation.class); 
    
    /** */
    private int line;
    
    /** */
    public LineLocation(int line) {
        this.line = line;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    @Override
    public String toString() {
        return res.getStr("line",Integer.toString(line));
    }
}
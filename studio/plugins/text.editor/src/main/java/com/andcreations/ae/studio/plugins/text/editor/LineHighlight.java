package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.Color;
import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class LineHighlight {
    /** The tag returned by the text area. */
    private Object tag;
    
    /** */
    private File file;
    
    /** */
    private int line;
    
    /** */
    private Color color;
    
    /** */
    LineHighlight(File file,int line,Color color) {
        this.file = file;
        this.line = line;
        this.color = color;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    public Color getColor() {
        return color;
    }
    
    /** */
    void setTag(Object tag) {
        this.tag = tag;
    }
    
    /** */
    Object getTag() {
        return tag;
    }
}

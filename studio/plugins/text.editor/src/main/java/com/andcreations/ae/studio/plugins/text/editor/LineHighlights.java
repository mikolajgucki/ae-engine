package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LineHighlights {
    private static LineHighlights instance;
    
    /** */
    private List<LineHighlight> highlights = new ArrayList<>();
    
    /** */
    private LineHighlightsListener listener;
    
    /** */
    public LineHighlight addLineHighlight(File file,int line,Color color) {
        LineHighlight highlight = new LineHighlight(file,line,color);
        highlights.add(highlight);
        
        listener.lineHighlightAdded(highlight);
        return highlight;
    }
    
    /** */
    public void removeLineHighlight(LineHighlight highlight) {
        listener.lineHighlightRemoved(highlight);
    }
    
    /** */
    List<LineHighlight> getHighlights(File file) {
        List<LineHighlight> fileHighlights = new ArrayList<>();
        
        for (LineHighlight highlight:highlights) {
            if (highlight.getFile().equals(file) == true) {
                fileHighlights.add(highlight);
            }
        }
        
        return fileHighlights;
    }
    
    /** */
    boolean hasHighlight(File file,int line) {
        for (LineHighlight highlight:highlights) {
            if (highlight.getFile().equals(file) == true &&
                highlight.getLine() == line) {
            //
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    void init(LineHighlightsListener listener) {
        this.listener = listener;
    }
    
    /** */
    public static LineHighlights get() {
        if (instance == null) {
            instance = new LineHighlights();
        }
        
        return instance;
    }
}
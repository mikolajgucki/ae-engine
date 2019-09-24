package com.andcreations.ae.studio.plugins.text.editor;

import com.andcreations.ae.color.Color;

/**
 * @author Mikolaj Gucki
 */
public class EditorAnnotation {
    /** */
    public static enum Priority {
        /** */
        HIGH(3),
        
        /** */
        MEDIUM(2),
        
        /** */
        LOW(1);
        
        /** */
        private int value;
        
        /** */
        private Priority(int value) {
            this.value = value;
        }
        
        /** */
        int getValue() {
            return value;
        }
    };
    
    /** */
    private int line;
    
    /** */
    private double position;
    
    /** */
    private String tooltip;
    
    /** */
    private Color color;    
    
    /** */
    private Priority priority;
    
    /** */
    EditorAnnotation(int line,String tooltip,Color color,Priority priority) {
        this.line = line;
        this.tooltip = tooltip;
        this.color = color;
        this.priority = priority;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    public String getTooltip() {
        return tooltip;
    }
    
    /** */
    public Color getColor() {
        return color;
    }
    
    /** */
    public Priority getPriority() {
        return priority;
    }
    
    /** */
    void setNormalizedPosition(double position) {
        this.position = position;
    }
    
    /** */
    double getNormalizedPosition() {
        return position;
    }
}
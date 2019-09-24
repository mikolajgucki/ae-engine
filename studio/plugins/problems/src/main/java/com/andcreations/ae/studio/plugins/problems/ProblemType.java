package com.andcreations.ae.studio.plugins.problems;

/**
 * Represents a problem type.
 *
 * @author Mikolaj Gucki
 */
public class ProblemType {
    /** */
    private String displayText;
    
    /** */
    public ProblemType(String displayText) {
        this.displayText = displayText;
    }
    
    /** */
    public String getDisplayText() {
        return displayText;
    }
}
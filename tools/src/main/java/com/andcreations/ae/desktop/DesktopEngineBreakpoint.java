package com.andcreations.ae.desktop;

/**
 * Represents a breakpoint.
 *
 * @author Mikolaj Gucki
 */
public class DesktopEngineBreakpoint {
    /** The source file. */
    private String source;
    
    /** The line number. */
    private int line;
    
    /**
     * Constructs a {@link DesktopEngineBreakpoint}.
     *
     * @param source The source file.
     * @param line The line number.
     */
    DesktopEngineBreakpoint(String source,int line) {
        this.source = source;
        this.line = line;
    }
    
    /**
     * Gets the source file.
     *
     * @return The source file.
     */
    String getSource() {
        return source;
    }
    
    /**
     * Gets the line number.
     *
     * @return The line number.
     */
    int getLine() {
        return line;
    }
}
package com.andcreations.ant;

import java.io.IOException;

/**
 * Thrown when Ant build fails.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class AntBuildFailedException extends IOException {
    /** */
    private int exitValue;
    
    /** */
    AntBuildFailedException(int exitValue) {
        this.exitValue = exitValue;
    }
    
    /** */
    public int getExitValue() {
        return exitValue;
    }
}
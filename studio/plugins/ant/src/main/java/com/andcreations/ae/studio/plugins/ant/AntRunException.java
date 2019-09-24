package com.andcreations.ae.studio.plugins.ant;

import java.io.IOException;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class AntRunException extends IOException {
    /** */
    private String output;
    
    /** */
    AntRunException(Throwable cause,String output) {
        super(cause.getMessage(),cause);
        this.output = output;
    }
    
    /**
     * Gets the Ant output.
     *
     * @return The Ant output.
     */
    public String getOutput() {
        return output;
    }
}
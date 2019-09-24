package com.andcreations.ae.tex.pack;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class TexPackException extends Exception {
    /** */
    public TexPackException() {
    }
    
    /** */
    public TexPackException(String message) {
        super(message);    
    }
    
    /** */
    public TexPackException(String message,Throwable cause) {
        super(message,cause);    
    }
}
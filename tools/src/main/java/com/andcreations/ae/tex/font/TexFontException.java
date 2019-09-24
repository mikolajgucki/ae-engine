package com.andcreations.ae.tex.font;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class TexFontException extends Exception {
    /** */
    TexFontException(String message) {
        super(message);
    }
    
    /** */
    TexFontException(String message,Throwable cause) {
        super(message,cause);
    }
}
package com.andcreations.xml;

import java.io.IOException;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class XMLException extends IOException {
    /** */
    public XMLException(String message) {
        super(message);
    }
    
    /** */
    public XMLException(String message,Throwable cause) {
        super(message,cause);
    }
}
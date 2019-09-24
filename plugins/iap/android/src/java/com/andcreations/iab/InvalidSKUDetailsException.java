package com.andcreations.iab;

/**
 * Thrown if the SKU details cannot be parsed.
 *
 * @author Mikolaj Gucki
 */
public class InvalidSKUDetailsException extends Exception {
    /** */
    InvalidSKUDetailsException(String message) {
        super(message);
    }
    
    /** */
    InvalidSKUDetailsException(String message,Throwable cause) {
        super(message,cause);
    }
}
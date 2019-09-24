package com.andcreations.android.adb;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class ADBException extends Exception {
    /** */
    public ADBException(String message) {
        super(message);
    }
    
    /** */
    public ADBException(String message,Throwable cause) {
        super(message,cause);
    }
    
    /** */
    public ADBException(Throwable cause) {
        super(cause.getMessage(),cause);
    }
}
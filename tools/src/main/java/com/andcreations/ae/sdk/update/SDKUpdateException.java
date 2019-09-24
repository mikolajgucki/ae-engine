package com.andcreations.ae.sdk.update;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class SDKUpdateException extends Exception {
    /** */
    public SDKUpdateException(String msg) {
        super(msg);
    }
    
    /** */
    public SDKUpdateException(String msg,Throwable cause) {
        super(msg,cause);
    }
}
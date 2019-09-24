package com.andcreations.lang;

/**
 * Provides throwable-related utility methods.
 *
 * @author Mikolaj Gucki
 */
public class ThrowableUtil {
    /** */
    public static String getRootCauseMessage(Throwable exception) {
        String message = exception.getMessage();
        Throwable cause = exception.getCause();
        while (cause != null) {
            message = cause.getMessage();
            cause = cause.getCause();
        }        
        
        return message;
    }
}
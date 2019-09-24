package com.andcreations.io.json;

import java.io.IOException;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class JSONException extends IOException {
    /** */
    public JSONException(String msg) {
        super(msg);
    }
    
    /** */
    public JSONException(String msg,Throwable cause) {
        super(msg,cause);
    }
}
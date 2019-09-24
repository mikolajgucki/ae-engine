package com.andcreations.ae.studio.plugin;

/**
 * Throws on plugin error.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class PluginException extends Exception {
    /** */
    public PluginException(String detail) {
        super(detail);
    }
    
    /** */    
    public PluginException(String detail,Throwable cause) {
        super(detail,cause);
    }
    
    /** */    
    public PluginException(Throwable cause) {
        super(cause.getMessage(),cause);
    }
}

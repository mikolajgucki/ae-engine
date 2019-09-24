package com.andcreations.ae.studio.plugins.builders;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class UnknownBuilderException extends RuntimeException {
    /** */
    private String builderId;
    
    /** */
    public UnknownBuilderException(String builderId) {
        super(String.format("Unknown builder %s",builderId));
        this.builderId = builderId;
    }
    
    /** */
    public String getBuilderId() {
        return builderId;
    }
}
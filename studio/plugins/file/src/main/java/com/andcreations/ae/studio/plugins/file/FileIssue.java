package com.andcreations.ae.studio.plugins.file;

/**
 * Represents a file issue.
 * 
 * @author Mikolaj Gucki
 */
public class FileIssue {
    /** The severity. */
    private FileIssueSeverity severity;
    
    /** The identifier of the issue source (typically plugin identifier). */
    private String sourceId;
    
    /** The issue message. */
    private String message;
    
    /** The location of the issue in the file. */
    private Object location;
    
    /**
     * Creates a {@link FileIssue}.
     *
     * @param severity The severity.
     * @param sourceId The identifier of the issue source (typically plugin
     *   identifier).
     * @param message The issue message.
     * @param location The location of the issue in the file.
     */
    public FileIssue(FileIssueSeverity severity,String sourceId,String message,
        Object location) {
    //
        this.severity = severity;
        this.sourceId = sourceId;
        this.message = message;
        this.location = location;
    }
    
    /**
     * Creates a {@link FileIssue}.
     *
     * @param severity The severity.
     * @param sourceId The identifier of the issue source (typically plugin
     *   identifier).
     * @param message The issue message.
     */
    public FileIssue(FileIssueSeverity severity,String sourceId,
        String message) {
    //
        this(severity,sourceId,message,null);
    }
    
    /**
     * Gets the severity.
     *
     * @return The severity.
     */
    public FileIssueSeverity getSeverity() {
        return severity;
    }
    
    /**
     * Gets the source identifier.
     *
     * @return The source identifier.
     */     
    public String getSourceId() {
        return sourceId;
    }
    
    /**
     * Gets the issue message.
     *
     * @return The issue message.
     */     
    public String getMessage() {
        return message;
    }
    
    /**
     * Gets the location.
     *
     * @return The location.
     */     
    public Object getLocation() {
        return location;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s: %s",severity.name(),message);
    }
}
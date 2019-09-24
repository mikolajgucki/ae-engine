package com.andcreations.ae.studio.plugins.problems;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a problem.
 *
 * @author Mikolaj Gucki
 */
public class Problem {
    /** */
    private String sourceId;
    
    /** */
    private ProblemSeverity severity;
    
    /** */
    private String description;
    
    /** */
    private String resource;
    
    /** */
    private String location;
    
    /** */
    private ProblemType type;
    
    /** */
    private List<ProblemListener> listeners = new ArrayList<>();
    
    /**
     * Constructs a {@link Problem}.
     *
     * @param sourceId The source identifier (typically plugin identifier).
     * @param severity The severity.
     * @param description The description.
     * @param type The problem type.
     */
    public Problem(String sourceId,ProblemSeverity severity,String description,
        ProblemType type) {
    //
        this.sourceId = sourceId;
        this.severity = severity;
        this.description = description;
        this.type = type;
    }
    
    /**
     * Constructs a {@link Problem}.
     *
     * @param sourceId The source identifier (typically plugin identifier).
     * @param severity The severity.
     * @param description The description.
     */
    public Problem(String sourceId,ProblemSeverity severity,
        String description) {
    //
        this(sourceId,severity,description,null);
    }    
    
    /** */
    public String getSourceId() {
        return sourceId;
    }
    
    /** */
    public ProblemSeverity getSeverity() {
        return severity;
    }

    /** */
    public String getDescription() {
        return description;
    }
    
    /** */
    public void setResource(String resource) {
        this.resource = resource;
    }
    
    /** */
    public String getResource() {
        return resource;
    }
    
    /** */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /** */
    public String getLocation() {
        return location;
    }
    
    /** */
    public void setType(ProblemType type) {
        this.type = type;
    }
    
    /** */
    public ProblemType getType() {
        return type;
    }
    
    /** */
    String getTypeDisplayText() {
        if (type == null) {
            return null;
        }
        return type.getDisplayText();
    }
    
    /** */
    public synchronized void addProblemListener(ProblemListener listener) {
        listeners.add(listener);
    }
    
    /** */
    public synchronized void removeProblemListener(ProblemListener listener) {
        listeners.remove(listener);
    }
    
    /** */
    synchronized void notifyDoubleClicked() {
        for (ProblemListener listener:listeners) {
            listener.problemDoubleClicked(this);
        }
    }
}
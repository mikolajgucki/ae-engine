package com.andcreations.ae.project;

/**
 * Thrown when a variable is not found.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class NoSuchVariableException extends Exception {
    /** The variable name. */
    private String name;
    
    /** The name of the variable referring the unknown variable. */
    private String referringVarName;
    
    /**
     * Constructs a {@link NoSuchVariableException}.
     *
     * @param name The variable name.
     */
    public NoSuchVariableException(String name) {
        super(name);
        this.name = name;
    }
    
    /**
     * Gets the variable name.
     * 
     * @return The variable name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the variable referring the unknown variable.
     *
     * @param referringVarName The name.
     */
    public void setReferringVarName(String referringVarName) {
        this.referringVarName = referringVarName;
    }
    
    /**
     * Gets the name of the variable referring the unknown variable.
     *
     * @return The name.
     */
    public String getReferringVarName() {
        return referringVarName;
    }
}
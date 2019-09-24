package com.andcreations.ant;

import org.apache.tools.ant.BuildException;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Represents a nested element of an Ant task with a value given as text.
 * 
 * @author Mikolaj Gucki
 */
public class AntSingleValue {
    /** The string resources. */
    private StrResources res = new BundleResources(AntSingleValue.class);
    
    /** The nested element value. */
    private String value;
    
    /**
     * Verifies that the value is not already set.
     */
    private void verifyValueNotSet() {
        if (value != null) {
            throw new BuildException(res.getStr("value.already.set"));
        }
    }
    
    /**
     * Sets the value.
     * 
     * @param value The value.
     */
    public void setValue(String value) {
        verifyValueNotSet();
        this.value = value;
    }
    
    /**
     * Sets the nested element text.
     * 
     * @param text The text.
     */
    public void addText(String text) {
        verifyValueNotSet();
        this.value = text;
    }
    
    /**
     * Gets the value of the nested element.
     * 
     * @return The nested element value.
     */
    public String getValue() {
        return value;
    }
}
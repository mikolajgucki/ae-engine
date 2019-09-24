package com.andcreations.ae.studio.plugins.lua.lib.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an assignment.
 *
 * @author Mikola Gucki
 */
public class Assignment extends LuaElement {
    /** The variable names. */
    private List<String> names;
    
    /** */
    Assignment(List<String> names,int beginLine,int endLine) {
        super(beginLine,endLine);
        this.names = new ArrayList<>(names);
    }
    
    /**
     * Gets the names of the left-hand variables.
     *
     * @return The variable names.
     */
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        for (String name:names) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(name);   
        }
        
        return builder.toString();        
    }
}
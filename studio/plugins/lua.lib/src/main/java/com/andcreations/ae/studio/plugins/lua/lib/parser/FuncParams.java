package com.andcreations.ae.studio.plugins.lua.lib.parser;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class FuncParams {
    /** */
    private List<String> names;
    
    /** */
    private boolean varArg;
    
    /** */
    FuncParams(List<String> names,boolean varArg) {
        this.names = names;
        this.varArg = varArg;
    }
    
    /** */
    public boolean isVarArg() {
        return varArg;
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

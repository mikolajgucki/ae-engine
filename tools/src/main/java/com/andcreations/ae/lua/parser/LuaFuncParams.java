package com.andcreations.ae.lua.parser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaFuncParams {
    /** */
    private List<String> names;
    
    /** */
    private boolean varArg;
    
    /** */
    LuaFuncParams(List<String> names,boolean varArg) {
        this.names = names;
        this.varArg = varArg;
    }
    
    /** */
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }
    
    /** */
    public boolean contains(String name) {
        return names.contains(name);
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
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof LuaFuncParams) == false) {
            return false;
        }
        LuaFuncParams that = (LuaFuncParams)obj;
        
    // names
        if (names.size() != that.names.size()) {
            return false;
        }
        for (int index = 0; index < names.size(); index++) {
            if (Objects.equals(names.get(index),
                that.names.get(index)) == false) {
            //
                return false;
            }        
        }
    // vararg
        if (varArg != that.varArg) {
            return false;
        }
        
        return true;
    }     
}

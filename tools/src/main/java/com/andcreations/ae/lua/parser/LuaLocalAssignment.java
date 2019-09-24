package com.andcreations.ae.lua.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Lua assignment.
 *
 * @author Mikola Gucki
 */
public class LuaLocalAssignment extends LuaStatement {
    /** The veriable names. */
    private List<String> names;
    
    /** The expressions. */
    private List<LuaExpression> exps;
    
    /** */
    LuaLocalAssignment(List<String> names,List<LuaExpression> exps,
        int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.names = new ArrayList<>(names);
        this.exps = new ArrayList<>(exps);
    }
    
    /**
     * Gets the variable names.
     *
     * @return The variable names.
     */
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }    
    
    /**
     * Gets the right-hand expressions.
     *
     * @return The expressions.
     */
    public List<LuaExpression> getExps() {
        return Collections.unmodifiableList(exps);
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        boolean firstName = true;
        for (String name:names) {
            if (firstName == false) {
                builder.append(",");
            }
            firstName = false;            
            builder.append(name);
        }
        builder.append("=");
        
        boolean firstExp = true;
        for (LuaExpression exp:exps) {
            if (firstExp == false) {
                builder.append(",");
            }
            firstExp = false;
            builder.append(exp.toString());
        }
        
        return builder.toString();     
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaLocalAssignment) == false) {
            return false;
        }
        LuaLocalAssignment that = (LuaLocalAssignment)obj;
        
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
    // expressions
        if (exps.size() != that.exps.size()) {
            return false;
        }
        for (int index = 0; index < exps.size(); index++) {
            if (Objects.equals(exps.get(index),that.exps.get(index)) == false) {
            //
                return false;                
            }
        }
        
        return true;
    }    
}
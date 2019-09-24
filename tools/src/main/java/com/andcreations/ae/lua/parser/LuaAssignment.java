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
public class LuaAssignment extends LuaStatement {
    /** The variables. */
    private List<LuaVariableExpression> vars;
    
    /** The expressions. */
    private List<LuaExpression> exps;
    
    /** */
    LuaAssignment(List<LuaVariableExpression> vars,List<LuaExpression> exps,
        int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.vars = new ArrayList<>(vars);
        this.exps = new ArrayList<>(exps);
    }
    
    /**
     * Gets the left-hand variables.
     *
     * @return The variables.
     */
    public List<LuaVariableExpression> getVars() {
        return Collections.unmodifiableList(vars);
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
        
        boolean firstVar = true;
        for (LuaVariableExpression var:vars) {
            if (firstVar == false) {
                builder.append(",");
            }
            firstVar = false;
            builder.append(var.toString());
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
        if ((obj instanceof LuaAssignment) == false) {
            return false;
        }        
        LuaAssignment that = (LuaAssignment)obj;
        
    // variables
        if (vars.size() != that.vars.size()) {
            return false;
        }
        for (int index = 0; index < vars.size(); index++) {
            if (Objects.equals(vars.get(index),that.vars.get(index)) == false) {
                return false;
            }
        }
    // expressions
        if (exps.size() != that.exps.size()) {
            return false;
        }
        for (int index = 0; index < exps.size(); index++) {
            if (Objects.equals(exps.get(index),that.exps.get(index)) == false) {
                return false;
            }
        }
            
        return true;
    }
}
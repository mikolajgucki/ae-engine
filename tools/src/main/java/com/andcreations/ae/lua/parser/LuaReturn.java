package com.andcreations.ae.lua.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaReturn extends LuaStatement {
    /** */
    private List<LuaExpression> exps;
    
    /** */
    LuaReturn(List<LuaExpression> exps,int beginLine,int endLine) {
        super(beginLine,endLine);
        this.exps = new ArrayList<>(exps);
    }
    
    /**
     * Gets the expressions.
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
        
        boolean firstExp = true;
        for (LuaExpression exp:exps) {
            if (firstExp == false) {
                builder.append(",");
            }
            firstExp = false;
            builder.append(exp.toString());
        }
        
        return String.format("return %s",builder.toString());
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaReturn) == false) {
            return false;
        }
        LuaReturn that = (LuaReturn)obj;
        
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
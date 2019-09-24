package com.andcreations.ae.lua.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaFuncArgs extends LuaElement {
    /** */
    private List<LuaExpression> exps;
    
    /** */
    LuaFuncArgs(List<LuaExpression> exps,int beginLine,int endLine) {
        super(beginLine,endLine);
        this.exps = new ArrayList<>(exps);
    }
    
    /** */
    public List<LuaExpression> getExps() {
        return Collections.unmodifiableList(exps);
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        for (LuaExpression exp:exps) {
            if (builder.length() > 0) {
                builder.append(",");
            }
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
        if ((obj instanceof LuaFuncArgs) == false) {
            return false;
        }
        LuaFuncArgs that = (LuaFuncArgs)obj;
        
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
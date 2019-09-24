package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaAnonFunc extends LuaExpression {
    /** */
    private LuaFuncParams params;
    
    /** */
    LuaAnonFunc(int beginLine,int endLine,LuaFuncParams params) {
        super(beginLine,endLine);
        this.params = params;
    }
    
    /**
     * Gets the function parameters.
     *
     * @return The function parameters.
     */
    public LuaFuncParams getParams() {
        return params;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("(%s)",params.toString());
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaAnonFunc) == false) {
            return false;
        }
        LuaAnonFunc that = (LuaAnonFunc)obj;
        
        if (Objects.equals(params,that.params) == false) {
            return false;
        }
        
        return true;
    }
}
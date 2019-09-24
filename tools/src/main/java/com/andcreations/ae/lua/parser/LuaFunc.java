package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaFunc extends LuaStatement {
    /** The function name. */
    private String name;
    
    /** */
    private LuaFuncParams params;
    
    /** */
    LuaFunc(String name,int beginLine,int endLine,LuaFuncParams params) {
        super(beginLine,endLine);
        this.name = name;
        this.params = params;
    }
    
    /**
     * Gets the function name.
     *
     * @return The function name.
     */
    public String getName() {
        return name;
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
        return String.format("%s(%s)",name,params.toString());
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaFunc) == false) {
            return false;
        }
        LuaFunc that = (LuaFunc)obj;
        
        if (Objects.equals(name,that.name) == false) {
            return false;
        }        
        if (Objects.equals(params,that.params) == false) {
            return false;
        }
        
        return true;
    }
}
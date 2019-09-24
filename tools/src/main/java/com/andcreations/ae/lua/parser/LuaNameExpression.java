package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaNameExpression extends LuaVariableExpression {
    /** */
    private String name;
    
    /** */
    LuaNameExpression(String name,int beginLine,int endLine) {
        super(beginLine,endLine);
        this.name = name;
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    @Override
    public String toString() {
        return name;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaNameExpression) == false) {
            return false;
        }
        LuaNameExpression that = (LuaNameExpression)obj;
        
    // name
        if (Objects.equals(name,that.name) == false) {
            return false;                
        }
        
        return true;
    }     
}
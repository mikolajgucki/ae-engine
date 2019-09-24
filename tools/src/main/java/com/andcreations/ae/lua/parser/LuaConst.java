package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaConst extends LuaExpression {
    /** */
    private LuaType type;  
    
    /** */
    private String value;
    
    /** */
    LuaConst(LuaType type,String value,int beginLine,int endLine) {
        super(beginLine,endLine);
        this.type = type;
        this.value = value;
    }
    
    /** */
    public LuaType getType() {
        return type;
    }
    
    /** */
    public String getValue() {
        return value;
    }
    
    /** */
    @Override
    public String toString() {
        return value;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaConst) == false) {
            return false;
        }
        LuaConst that = (LuaConst)obj;

        if (type != that.type) {
            return false;
        }
        if (Objects.equals(value,that.value) == false) {
            return false;
        }
        
        return true;        
    }
}
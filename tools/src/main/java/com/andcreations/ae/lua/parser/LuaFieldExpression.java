package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaFieldExpression extends LuaVariableExpression {
    /** */
    private LuaPrimaryExpression leftHandExp;
    
    /** */
    private String name;
    
    /** */
    LuaFieldExpression(LuaPrimaryExpression leftHandExp,String name,
        int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.leftHandExp = leftHandExp;
        this.name = name;
    }
        
    /** */
    public LuaPrimaryExpression getLeftHandExp() {
        return leftHandExp;
    }
    
    /** */
    public String getName() {
        return name;
    }    
    
    /** */
    @Override
    public String toString() {
        return String.format("%s.%s",leftHandExp.toString(),name);
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaFieldExpression) == false) {
            return false;
        }
        LuaFieldExpression that = (LuaFieldExpression)obj;
        
        if (Objects.equals(leftHandExp,that.leftHandExp) == false) {
            return false;
        }
        if (Objects.equals(name,that.name) == false) {
            return false;
        }
        
        return true;
    }
}
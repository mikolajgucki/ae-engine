package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaIndexExpression extends LuaVariableExpression {
    /** */
    private LuaPrimaryExpression leftHandExp;
    
    /** */
    private LuaExpression exp;
    
    /** */
    LuaIndexExpression(LuaPrimaryExpression leftHandExp,LuaExpression exp,
        int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.leftHandExp = leftHandExp;
        this.exp = exp;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s[%s]",leftHandExp.toString(),exp.toString());
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaIndexExpression) == false) {
            return false;
        }
        LuaIndexExpression that = (LuaIndexExpression)obj;
        
    // left-hand expression
        if (Objects.equals(leftHandExp,that.leftHandExp) == false) {
            return false;
        }
    // right-hand expression
        if (Objects.equals(exp,that.exp) == false) {
            return false;
        }
        
        return true;
    }    
}
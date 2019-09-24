package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaBinopExpression extends LuaExpression {
    /** */
    private LuaExpression leftHandExp;
    
    /** */
    private int operator;
    
    /** */
    private LuaExpression rightHandExp;
    
    LuaBinopExpression(LuaExpression leftHandExp,int operator,
        LuaExpression rightHandExp,int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.leftHandExp = leftHandExp;
        this.operator = operator;
        this.rightHandExp = rightHandExp;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaBinopExpression) == false) {
            return false;
        }        
        LuaBinopExpression that = (LuaBinopExpression)obj;
        
        if (Objects.equals(leftHandExp,that.leftHandExp) == false) {
            return false;
        }
        if (operator != that.operator) {
            return false;
        }
        if (Objects.equals(rightHandExp,that.rightHandExp) == false) {
            return false;
        }
        
        return true;
    }
}
    
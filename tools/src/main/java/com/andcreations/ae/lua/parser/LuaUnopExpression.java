package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaUnopExpression extends LuaExpression {
    /** */
    private int operator;
    
    /** */
    private LuaExpression rightHandExp;
    
    /** */
    LuaUnopExpression(int operator,LuaExpression rightHandExp,int beginLine,
        int endLine) {
    //
        super(beginLine,endLine);
        this.operator = operator;
        this.rightHandExp = rightHandExp;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaUnopExpression) == false) {
            return false;
        }
        LuaUnopExpression that = (LuaUnopExpression)obj;
        
    // operator
        if (operator != that.operator) {
            return false;
        }
    // expression
        if (Objects.equals(rightHandExp,that.rightHandExp) == false) {
            return false;
        }            
        
        return true;
    }     
}
    
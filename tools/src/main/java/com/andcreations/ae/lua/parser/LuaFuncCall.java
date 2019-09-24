package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaFuncCall extends LuaPrimaryExpression {
    /** */
    private LuaPrimaryExpression leftHandExp;
    
    /** */
    private LuaFuncArgs args;
    
    /** */
    LuaFuncCall(LuaPrimaryExpression leftHandExp,LuaFuncArgs args,
        int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.leftHandExp = leftHandExp;
        this.args = args;
    }
    
    /** */
    public LuaPrimaryExpression getLeftHandExp() {
        return leftHandExp;
    }
    
    /** */
    public LuaFuncArgs getArgs() {
        return args;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s(%s)",leftHandExp.toString(),args.toString());
    }    
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaFuncCall) == false) {
            return false;
        }
        LuaFuncCall that = (LuaFuncCall)obj;
        
    // expression
        if (Objects.equals(leftHandExp,that.leftHandExp) == false) {
            return false;
        }
    // arguments
        if (Objects.equals(args,that.args) == false) {
            return false;
        }
        
        return true;
    }      
}
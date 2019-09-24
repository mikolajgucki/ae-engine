package com.andcreations.ae.lua.parser;

import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaTableField extends LuaElement {
    /** */
    private LuaExpression index;
    
    /** */
    private String name;
    
    /** */
    private LuaExpression rightHandExp;
    
    /** */
    LuaTableField(LuaExpression index,String name,LuaExpression rightHandExp,
        int beginLine,int endLine) {
    //
        super(beginLine,endLine);
        this.index = index;
        this.name = name;
        this.rightHandExp = rightHandExp;
    }
    
    /**
     * Gets the index.
     *
     * @return The index.
     */
    public LuaExpression getIndex() {
        return index;
    }
    
    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the right-hand expression.
     *
     * @return The right-hand expression.
     */
    public LuaExpression getRightHandExp() {
        return rightHandExp;
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (index != null) {
            builder.append(index.toString());
        }
        if (name != null) {
            builder.append(name);
        }
        if (rightHandExp != null) {
            builder.append("=" + rightHandExp.toString());
        }
        builder.append("\n");
        return builder.toString();
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaTableField) == false) {
            return false;
        }        
        LuaTableField that = (LuaTableField)obj;
        
        if (Objects.equals(index,that.index) == false) {
            return false;
        }
        if (Objects.equals(name,that.name) == false) {
            return false;
        }
        if (Objects.equals(rightHandExp,that.rightHandExp) == false) {
            return false;
        }
        
        return true;
    }    
}
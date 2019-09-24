package com.andcreations.ae.lua.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mikolaj Gucki
 */
public class LuaTableConstructor extends LuaExpression {
    /** */
    private List<LuaTableField> fields;
    
    /** */
    LuaTableConstructor(List<LuaTableField> fields,int beginLine,int endLine) {
        super(beginLine,endLine);
        this.fields = new ArrayList<>(fields);
    }
    
    /**
     * Gets the fields.
     *
     * @return The fields.
     */
    public List<LuaTableField> getFields() {
        return fields;
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (LuaTableField field:fields) {
            builder.append(field.toString());
        }
        builder.append("}");
        return builder.toString();
    }
    
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) {
            return false;
        }
        if ((obj instanceof LuaTableConstructor) == false) {
            return false;
        }        
        LuaTableConstructor that = (LuaTableConstructor)obj;
        
    // fields
        if (fields.size() != that.fields.size()) {
            return false;
        }
        for (int index = 0; index < fields.size(); index++) {
            if (Objects.equals(fields.get(index),
                that.fields.get(index)) == false) {
            //
                return false;
            }
        }            
        
        return true;
    }    
}
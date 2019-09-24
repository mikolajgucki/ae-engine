package com.andcreations.ae.desktop;

/**
 * @author Mikolaj Gucki
 */
public class LuaValue {
    /** */
    public static enum Scope {
        /** */
        UNDEFINED,
        
        /** */
        LOCAL,
        
        /** */
        UPVALUE,
        
        /** */
        GLOBAL;
    }
    
    /** */
    public static enum Type {
        /** */
        NIL,
        
        /** */
        NUMBER,
        
        /** */
        BOOLEAN,
        
        /** */
        STRING,
        
        /** */
        TABLE,
        
        /** */
        FUNCTION,
        
        /** */
        USER_DATA,
        
        /** */
        THREAD,
        
        /** */
        LIGHT_USER_DATA;        
    }
    
    /** */
    private String name;
    
    /** */
    private String value;
    
    /** */
    private Scope scope;
    
    /** */
    private Type type;
    
    /** */
    private String pointer;
    
    /** */
    LuaValue(String name,String value,Scope scope,Type type) {
        this.name = name;
        this.value = value;
        this.scope = scope;
        this.type = type;
    }
    
    /**
     * Gets the value.
     *
     * @return The value.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the value.
     *
     * @return The value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Gets the scope.
     *
     * @return The scope.
     */
    public Scope getScope() {
        return scope;
    }
    
    /**
     * Gets the type.
     *
     * @return The type.
     */
    public Type getType() {
        return type;
    }
    
    /** */
    void setPointer(String pointer) {
        this.pointer = pointer;
    }
    
    /**
     * Gets the table pointer.
     *
     * @return The table pointer.
     */
    public String getPointer() {
        return pointer;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (pointer == null) {
            return false;
        }
        if (!(obj instanceof LuaValue)) {
            return false;
        }
        LuaValue that = (LuaValue)obj;
        if (that.getPointer() == null) {
            return false;
        }
        return pointer.equals(that.getPointer());
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s = %s [%s, %s]",name,value,
            scope.name().toLowerCase(),type.name().toLowerCase());
    }
}
package com.andcreations.ae.desktop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaTracebackItem {
    /** */
    public static enum What {
        /** */
        LUA_FUNCTION,
        
        /** */
        C_FUNCTION,
        
        /** */
        MAIN_CHUNK,

        /** */
        TAIL_FUNCTION,

        /** */
        UNKNOWN;        
    }
    
    /** */
    private int index;
    
    /** */
    private String source;
    
    /** */
    private int line;
    
    /** */
    private What what;
    
    /** */
    private String name;
    
    /** */
    private List<LuaValue> values = new ArrayList<>();
    
    /** */
    LuaTracebackItem(int index,String source,int line,What what,
        String name,List<LuaValue> values) {
    //
        this.index = index;
        this.source = source;
        this.line = line;
        this.what = what;
        this.name = name;
        this.values = values;
    }
    
    /**
     * Gets the index of the item in the call stack.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Gets the name of the source file.
     *
     * @return The name of the source file.
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Gets the current line in the source file.
     *
     * @return The line number.
     */
    public int getLine() {
        return line;
    }
    
    /**
     * Gets the type of a traceback item.
     *
     * @return The name.
     */
    public What getWhat() {
        return what;
    }
    
    /**
     * Gets the function name.
     *
     * @return The function name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the values.
     *
     * @return The values.
     */
    public List<LuaValue> getValues() {
        return Collections.unmodifiableList(values);
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s:%d",source,line);
    }
}
package com.andcreations.ae.studio.plugins.lua.lib.parser;

/**
 * @author Mikolaj Gucki
 */
public class Func extends LuaElement {
    /** The function name. */
    private String name;
    
    /** */
    private FuncParams params;
    
    /** */
    Func(String name,int beginLine,int endLine,FuncParams params) {
        super(beginLine,endLine);
        this.name = name;
        this.params = params;
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
     * Gets the function parameters.
     *
     * @return The function parameters.
     */
    public FuncParams getParams() {
        return params;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s(%s)",name,params.toString());
    }
}
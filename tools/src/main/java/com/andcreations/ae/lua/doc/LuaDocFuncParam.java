package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocFuncParam {
    /** */
    public static final String WARNING_UNKNOWN_PARAM = "unknown.func.param";
    
    /** */
    public static final String WARNING_MISSING_PARAM = "missing.func.param";
    
    /** */
    public static final String ERROR_NO_PARAM_NAME = "no.param.name";
    
    /** */
    public static final String VAR_ARG = "...";
    
    /** */
    private String name;
    
    /** */
    private String desc;
    
    /** */
    private int line;
    
    /** */
    public void setName(String name) {
        this.name = name;
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    /** */
    public String getDesc() {
        return desc;
    }   
    
    /** */
    public void setLine(int line) {
        this.line = line;
    }
    
    /** */
    public int getLine() {
        return line;
    }    
}    
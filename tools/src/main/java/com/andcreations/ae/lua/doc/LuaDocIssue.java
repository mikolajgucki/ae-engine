package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocIssue {
    /** */
    private String type;
    
    /** */
    private String value;
    
    /** */
    private String message;
    
    /** */
    private int line;
    
    /** */
    LuaDocIssue(String type,String value,String message,int line) {
        this.type = type;
        this.value = value;
        this.message = message;
        this.line = line;        
    }
    
    /** */
    public String getType() {
        return type;
    }
    
    /** */
    public String getValue() {
        return value;
    }
    
    /** */
    public String getMessage() {
        return message;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s@%d: %s",type,line,message);
    }
    
    /** */
    static void addWarning(LuaDocParseContext context,String type,
        String value,String message,int line) {
    //
        context.getFileData().addWarning(
            new LuaDocWarning(type,value,message,line));
    }
    
    /** */
    static void addError(LuaDocParseContext context,String type,
        String value,String message,int line) {
    //
        context.getFileData().addError(
            new LuaDocError(type,value,message,line));
    }    
}
package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocError extends LuaDocIssue {
    /** */
    LuaDocError(String type,String value,String message,int line) {
        super(type,value,message,line);
    }
}
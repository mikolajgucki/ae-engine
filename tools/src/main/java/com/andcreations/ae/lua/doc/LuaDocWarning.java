package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocWarning extends LuaDocIssue {
    /** */
    LuaDocWarning(String type,String value,String message,int line) {
        super(type,value,message,line);
    }
}
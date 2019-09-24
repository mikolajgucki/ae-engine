package com.andcreations.ae.lua.parser;

/**
 * @author Mikolaj Gucki
 */
public class LuaLocalFunc extends LuaFunc {
    /** */
    LuaLocalFunc(String name,int beginLine,int endLine,LuaFuncParams params) {
        super(name,beginLine,endLine,params);
    }
}
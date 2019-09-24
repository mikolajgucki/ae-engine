package com.andcreations.lua.parser;

import java.io.File;

import com.andcreations.lua.runner.LuaRunner;

/**
 * @author Mikolaj Gucki
 */
public class Test {
    /** */
    public static void main(String[] args) throws Exception {
        LuaRunner lr = new LuaRunner(new File("f:\\1\\lua.exe"));
        LuaStringParser p = new LuaStringParser(lr);
        System.out.println(p.parse("[['foobar'\"\\u{030}\\064]]",new File("p.lua")));
    }
}
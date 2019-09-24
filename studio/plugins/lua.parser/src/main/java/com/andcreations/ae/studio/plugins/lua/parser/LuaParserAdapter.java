package com.andcreations.ae.studio.plugins.lua.parser;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public abstract class LuaParserAdapter implements LuaParserListener {
    /**
     * Called when a Lua file has been parsed.
     *
     * @param parsedFile The parsed file.
     */
    @Override
    public void luaFileParsed(LuaParsedFile parsedFile) {
    }
    
    /**
     * Called when parsing a Lua file has failed.
     *
     * @param file The file.
     * @param message The error message.
     */
    @Override
    public void luaFileParsingFailed(File file,String message) {
    }
}
package com.andcreations.ae.studio.plugins.lua.parser;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface LuaParserListener {
    /**
     * Called when a Lua file has been parsed.
     *
     * @param parsedFile The parsed file.
     */
    void luaFileParsed(LuaParsedFile parsedFile);
    
    /**
     * Called when parsing a Lua file has failed.
     *
     * @param file The file.
     * @param message The error message.
     */
    void luaFileParsingFailed(File file,String message);
}
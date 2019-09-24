package com.andcreations.ae.studio.plugins.lua.parser;

import java.io.File;

import com.andcreations.ae.lua.doc.LuaDocFileData;
import com.andcreations.ae.lua.parser.LuaFileInfo;

/**
 * @author Mikolaj Gucki
 */
public class LuaParsedFile {
    /** */
    private File file;
    
    /** */
    private LuaFileInfo info;
    
    /** */
    private LuaDocFileData luaDocInfo;
    
    /** */
    LuaParsedFile(File file,LuaFileInfo info,LuaDocFileData luaDocInfo) {
        this.file = file;
        this.info = info;
        this.luaDocInfo = luaDocInfo;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public LuaFileInfo getInfo() {
        return info;
    }
    
    /** */
    public LuaDocFileData getLuaDocInfo() {
        return luaDocInfo;
    }
}
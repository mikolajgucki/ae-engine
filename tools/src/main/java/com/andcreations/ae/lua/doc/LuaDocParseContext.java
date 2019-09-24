package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocParseContext {
    /** */
    private LuaDocFileData fileData;
    
    /** */
    public void setFileData(LuaDocFileData fileData) {
        this.fileData = fileData;
    }
    
    /** */
    public LuaDocFileData getFileData() {
        return fileData;
    }
}
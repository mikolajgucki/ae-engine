package com.andcreations.ae.tex.pack.gen;

/**
 * @author Mikolaj Gucki
 */
public class TexPackSrcGenCfg {
    /** */
    private String id;
    
    /** */
    private String imageFileName;
    
    /** */
    private String luaFileName;
    
    /** */
    private TexPackGenResult result;
    
    /** */
    public TexPackSrcGenCfg(String id,String imageFileName,String luaFileName,
        TexPackGenResult result) {
    //
        this.id = id;
        this.imageFileName = imageFileName;
        this.luaFileName = luaFileName;
        this.result = result;
    }
    
    /** */
    String getId() {
        return id;
    }
    
    /** */
    String getImageFileName() {
        return imageFileName;
    }
    
    /** */
    String getLuaFileName() {
        return luaFileName;
    }    
    
    /** */
    TexPackGenResult getResult() {
        return result;
    }
}
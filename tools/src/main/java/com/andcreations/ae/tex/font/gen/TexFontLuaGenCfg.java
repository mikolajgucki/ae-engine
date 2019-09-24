package com.andcreations.ae.tex.font.gen;

/**
 * @author Mikolaj Gucki
 */
public class TexFontLuaGenCfg {
    /** */
    private String id;
    
    /** */
    private String texture;
    
    /** */
    private String fileName;
    
    /** */
    private TexFontGenResult result;
    
    /** */
    public TexFontLuaGenCfg(String id,String texture,String fileName,
        TexFontGenResult result) {
    //
        this.id = id;
        this.texture = texture;
        this.fileName = fileName;
        this.result = result;
    }
    
    /** */
    String getId() {
        return id;
    }
    
    /** */
    String getTexture() {
        return texture;
    }
    
    /** */
    String getFileName() {
        return fileName;
    }
    
    /** */
    TexFontGenResult getResult() {
        return result;
    }
}
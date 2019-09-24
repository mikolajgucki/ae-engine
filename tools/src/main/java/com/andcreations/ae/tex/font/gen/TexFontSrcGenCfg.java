package com.andcreations.ae.tex.font.gen;

/**
 * @author Mikolaj Gucki
 */
public class TexFontSrcGenCfg {
    /** */
    private String id;
    
    /** */
    private String fileName;
    
    /** */
    private TexFontGenResult result;
    
    /** */
    public TexFontSrcGenCfg(String id,String fileName,
        TexFontGenResult result) {
    //
        this.id = id;
        this.fileName = fileName;
        this.result = result;
    }
    
    /** */
    String getId() {
        return id;
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
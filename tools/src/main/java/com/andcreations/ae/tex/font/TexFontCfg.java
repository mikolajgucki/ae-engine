package com.andcreations.ae.tex.font;

import java.io.File;

import com.andcreations.ae.tex.font.data.TexFontData;

/**
 * @author Mikolaj Gucki
 */
public class TexFontCfg {
    /** The font identifier. */
    private String id;
    
    /** The font data. */
    private TexFontData data;
    
    /** The path to the output image from which subtextures are extracted. */
    private File imageFile;
    
    /** The output Lua file. */
    private File luaFile;
    
    /** The output raw file. */
    private File rawFile;
    
    /** The output mask file. */
    private File maskFile;
    
    /** The output data file. */
    private File dataFile;
    
    /** */
    public TexFontCfg(String id,TexFontData data) {
        this.id = id;
        this.data = data;
    }
    
    /** */
    public String getId() {
        return id;
    }
    
    /** */
    public TexFontData getData() {
        return data;
    }
    
    /** */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
    
    /** */
    public File getImageFile() {
        return imageFile;
    }
    
    /** */
    public void setLuaFile(File luaFile) {
        this.luaFile = luaFile;
    }
    
    /** */
    public File getLuaFile() {
        return luaFile;
    }
    
    /** */
    public void setRawFile(File rawFile) {
        this.rawFile = rawFile;
    }
    
    /** */
    public File getRawFile() {
        return rawFile;
    } 
        
    /** */
    public void setMaskFile(File maskFile) {
        this.maskFile = maskFile;
    }
    
    /** */
    public File getMaskFile() {
        return maskFile;
    }  

        
    /** */
    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }
    
    /** */
    public File getDataFile() {
        return dataFile;
    }    
}
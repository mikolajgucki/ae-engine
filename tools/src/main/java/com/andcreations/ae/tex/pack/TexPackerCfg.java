package com.andcreations.ae.tex.pack;

import java.io.File;

import com.andcreations.ae.tex.pack.data.TexPack;

/**
 * @author Mikolaj Gucki
 */
public class TexPackerCfg {
    /** */
    private String id;
    
    /** */
    private TexPack data;
    
    /** The output image with the texture. */
    private File imageFile;
    
    /** The output Lua file. */
    private File luaFile;
    
    /** */
    public TexPackerCfg(String id,TexPack data) {
        this.id = id;
        this.data = data;
    }
    
    /** */
    public String getId() {
        return id;
    }    
    
    /** */
    public TexPack getData() {
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
}